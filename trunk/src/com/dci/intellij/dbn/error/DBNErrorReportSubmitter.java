package com.dci.intellij.dbn.error;

import com.intellij.diagnostic.IdeErrorsDialog;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.openapi.extensions.PluginId;
import org.jetbrains.annotations.NonNls;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.FAILED;
import static com.intellij.openapi.diagnostic.SubmittedReportInfo.SubmissionStatus.NEW_ISSUE;

public class DBNErrorReportSubmitter extends ErrorReportSubmitter {
    private static final Logger log = Logger.getInstance(DBNErrorReportSubmitter.class.getName());
    @NonNls
    private static final String SERVER_URL = "http://dci.myjetbrains.com/youtrack/";
    private static final String SERVER_REST_URL = SERVER_URL + "rest/";
    private static final String SERVER_ISSUE_URL = SERVER_REST_URL + "issue";
    private static final String LOGIN_URL = SERVER_REST_URL + "user/login";

    private String description = null;
    private String extraInformation = "";
    private String affectedVersion = null;
    private static final String DEFAULT_RESPONSE = "Thank you for your report.";


    @Override
    public String getReportActionText() {
        return "Report Issue";
    }

    @Override
    public SubmittedReportInfo submit(IdeaLoggingEvent[] events, Component parentComponent) {
        this.description = events[0].getThrowableText().substring(0, Math.min(Math.max(80, events[0].getThrowableText().length()), 80));

        @NonNls StringBuilder descBuilder = new StringBuilder();

        String platformBuild = ApplicationInfo.getInstance().getBuild().asString();
        descBuilder.append("Platform Version: ").append(platformBuild).append('\n');
        Throwable t = events[0].getThrowable();
        if (t != null) {
            final PluginId pluginId = IdeErrorsDialog.findPluginId(t);
            if (pluginId != null) {
                final IdeaPluginDescriptor ideaPluginDescriptor = PluginManager.getPlugin(pluginId);
                if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled()) {
                    descBuilder.append("Plugin ").append(ideaPluginDescriptor.getName()).append(" version: ").append(ideaPluginDescriptor.getVersion()).append("\n");
                    this.affectedVersion = ideaPluginDescriptor.getVersion();
                }
            }
        }

        if (description == null) description = "<none>";

        descBuilder.append("\n\nDescription: ").append(description).append("\n\nUser: ").append("<none>");

        for (IdeaLoggingEvent e : events)
            descBuilder.append("\n\n").append(e.toString());

        this.extraInformation = descBuilder.toString();

        String result = submit();
        log.info("Error submitted, response: " + result);

        if (result == null)
            return new SubmittedReportInfo(SERVER_ISSUE_URL, "", FAILED);

        String ResultString = null;
        try {
            Pattern regex = Pattern.compile("id=\"([^\"]+)\"", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(result);
            if (regexMatcher.find()) {
                ResultString = regexMatcher.group(1);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }

        SubmittedReportInfo.SubmissionStatus status = NEW_ISSUE;

        if (ResultString == null)
            return new SubmittedReportInfo(SERVER_ISSUE_URL, "", FAILED);
//        else {
//            if (ResultString.trim().length() > 0)
//                status = DUPLICATE;
//        }

        return new SubmittedReportInfo(SERVER_URL + "issue/" + ResultString, ResultString, status);
    }

    public String submit() {
        if (this.description == null || this.description.length() == 0) throw new RuntimeException("Description");
        String project = "DBN";
        StringBuilder response = new StringBuilder("");

        //Create Post String
        StringBuilder data = new StringBuilder();
        try {

            // Build Login
            String userName = "autosubmit";
            data.append(URLEncoder.encode("login", "UTF-8")).append("=").append(URLEncoder.encode(userName, "UTF-8"));
            data.append("&").append(URLEncoder.encode("password", "UTF-8")).append("=").append(URLEncoder.encode("autosubmit", "UTF-8"));

            // Send Login
            URL url = new URL(LOGIN_URL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            // Get The Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }

            data = new StringBuilder();
            data.append(URLEncoder.encode("project", "UTF-8")).append("=").append(URLEncoder.encode(project, "UTF-8"));
            data.append("&").append(URLEncoder.encode("assignee", "UTF-8")).append("=").append(URLEncoder.encode("Unassigned", "UTF-8"));
            data.append("&").append(URLEncoder.encode("summary", "UTF-8")).append("=").append(URLEncoder.encode(description, "UTF-8"));
            data.append("&").append(URLEncoder.encode("description", "UTF-8")).append("=").append(URLEncoder.encode(extraInformation, "UTF-8"));
            data.append("&").append(URLEncoder.encode("priority", "UTF-8")).append("=").append(URLEncoder.encode("4", "UTF-8"));
            data.append("&").append(URLEncoder.encode("type", "UTF-8")).append("=").append(URLEncoder.encode("Exception", "UTF-8"));

            if (this.affectedVersion != null)
                data.append("&").append(URLEncoder.encode("affectsVersion", "UTF-8")).append("=").append(URLEncoder.encode(this.affectedVersion, "UTF-8"));

            // Send Data To Page
            url = new URL(SERVER_ISSUE_URL);

            conn = url.openConnection();
            conn.setDoOutput(true);

            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            // Get The Response
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = rd.readLine()) != null) {
                response.append(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}