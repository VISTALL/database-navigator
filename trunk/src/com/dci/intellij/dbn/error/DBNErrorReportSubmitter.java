package com.dci.intellij.dbn.error;

import com.dci.intellij.dbn.common.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.createLogger();

    private static final String URL = "http://dci.myjetbrains.com/youtrack/";
    private static final String ISSUE_URL = URL + "rest/issue";
    private static final String LOGIN_URL = URL + "rest/user/login";
    private static final String ENC = "UTF-8";

    @Override
    public String getReportActionText() {
        return "Report Issue";
    }

    @Override
    public SubmittedReportInfo submit(IdeaLoggingEvent[] events, Component parentComponent) {
        IdeaLoggingEvent firstEvent = events[0];
        String firstEventText = firstEvent.getThrowableText();
        String summary = firstEventText.substring(0, Math.min(Math.max(80, firstEventText.length()), 80));

        @NonNls StringBuilder description = new StringBuilder();

        String platformBuild = ApplicationInfo.getInstance().getBuild().asString();
        description.append("Platform Version: ").append(platformBuild).append('\n');
        Throwable t = firstEvent.getThrowable();
        String pluginVersion = null;
        if (t != null) {
            PluginId pluginId = IdeErrorsDialog.findPluginId(t);
            if (pluginId != null) {
                final IdeaPluginDescriptor ideaPluginDescriptor = PluginManager.getPlugin(pluginId);
                if (ideaPluginDescriptor != null && !ideaPluginDescriptor.isBundled()) {
                    pluginVersion = ideaPluginDescriptor.getVersion();
                    description.append("Plugin ").append(ideaPluginDescriptor.getName()).append(" version: ").append(pluginVersion).append("\n");
                }
            }
        }

        description.append("\n\nDescription: ").append(summary).append("\n\nUser: ").append("<none>");

        for (IdeaLoggingEvent event : events)
            description.append("\n\n").append(event.toString());

        String result = submit(pluginVersion, summary, description.toString());
        LOGGER.info("Error submitted, response: " + result);

        if (result == null)
            return new SubmittedReportInfo(ISSUE_URL, "", FAILED);

        String ticketId = null;
        try {
            Pattern regex = Pattern.compile("id=\"([^\"]+)\"", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(result);
            if (regexMatcher.find()) {
                ticketId = regexMatcher.group(1);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }


        return ticketId == null ?
                new SubmittedReportInfo(ISSUE_URL, "", FAILED) :
                new SubmittedReportInfo(URL + "issue/" + ticketId, ticketId, NEW_ISSUE);
    }

    public String submit(String pluginVersion, String summary, String description) {
        StringBuilder response = new StringBuilder("");

        StringBuilder data = new StringBuilder();
        try {
            data.append(URLEncoder.encode("login", ENC)).append("=").append(URLEncoder.encode("autosubmit", ENC));
            data.append("&").append(URLEncoder.encode("password", ENC)).append("=").append(URLEncoder.encode("autosubmit", ENC));

            URL url = new URL(LOGIN_URL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }

            data = new StringBuilder();
            data.append(URLEncoder.encode("project", ENC)).append("=").append(URLEncoder.encode("DBN", ENC));
            data.append("&").append(URLEncoder.encode("assignee", ENC)).append("=").append(URLEncoder.encode("Unassigned", ENC));
            data.append("&").append(URLEncoder.encode("summary", ENC)).append("=").append(URLEncoder.encode(summary, ENC));
            data.append("&").append(URLEncoder.encode("description", ENC)).append("=").append(URLEncoder.encode(description, ENC));
            data.append("&").append(URLEncoder.encode("priority", ENC)).append("=").append(URLEncoder.encode("4", ENC));
            data.append("&").append(URLEncoder.encode("type", ENC)).append("=").append(URLEncoder.encode("Exception", ENC));

            if (pluginVersion != null)
                data.append("&").append(URLEncoder.encode("affectsVersion", ENC)).append("=").append(URLEncoder.encode(pluginVersion, ENC));

            url = new URL(ISSUE_URL);
            conn = url.openConnection();
            conn.setDoOutput(true);

            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

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