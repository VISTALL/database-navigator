package com.dci.intellij.dbn.driver;

import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.util.ActionUtil;
import com.dci.intellij.dbn.common.util.NamingUtil;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DatabaseDriverManager implements ApplicationComponent {
    private Map<String, String[]> cache = new HashMap<String, String[]>();
    private Map<String, ClassLoader> classLoaders = new HashMap<String, ClassLoader>();

    public static DatabaseDriverManager getInstance() {
        return ApplicationManager.getApplication().getComponent(DatabaseDriverManager.class);
    }

    public DatabaseDriverManager() {
        //TODO make this configurable
        DriverManager.setLoginTimeout(30);
    }

    @NonNls
    @NotNull
    public String getComponentName() {
        return "DBNavigator.DatabaseDriverManager";
    }

    public void initComponent() {}
    public void disposeComponent() {}

    public String[] loadDriverClassesWithProgressBar(String libraryName) {
        LoaderThread loader = new LoaderThread(libraryName);
        Project project = ActionUtil.getProject();
        ProgressManager.getInstance().runProcessWithProgressSynchronously(loader, Constants.DBN_TITLE_PREFIX + "Loading database drivers" , false, project);
        return loader.getClasses();
    }

    class LoaderThread implements Runnable{
        public LoaderThread(String libraryName) {
            this.libraryName = libraryName;
        }

        String[] classes;
        String libraryName;
        public void run() {
            classes = loadDriverClasses(libraryName);
        }
        public String[] getClasses() {
                return classes;
        }
    }


    public String[] loadDriverClasses(String libraryName) {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (progressIndicator != null) {
            progressIndicator.setText("Loading jdbc drivers from " + libraryName);
        }
        String[] classNames = cache.get(libraryName);
        if (classNames == null && new File(libraryName).isFile()) {
            try {
                List<String> drivers = new ArrayList<String>();
                URL[] urls = new URL[]{new File(libraryName).toURL()};
                URLClassLoader classLoader = new URLClassLoader(urls);
                classLoaders.put(libraryName, classLoader);

                JarFile jarFile = new JarFile(libraryName);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class")) {

                        int index = name.lastIndexOf('.');
                        String className = name.substring(0, index);
                        className = className.replace('/', '.').replace('\\', '.');

                        try {
                            if (className.contains("Driver")) {
                                Class<?> clazz = classLoader.loadClass(className);
                                if (Driver.class.isAssignableFrom(clazz)) {
                                    drivers.add(clazz.getName());
                                } else {

                                }
                            }
                        }
                        catch(Throwable throwable) {
                            // ignore
                        }
                    }
                }
                classNames = drivers.toArray(new String[drivers.size()]);
                cache.put(libraryName, classNames);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return classNames == null ? new String[0] : classNames ;
    }

    public synchronized Driver getDriver(String libraryName, String className) throws Exception {
        if (StringUtil.isEmptyOrSpaces(className)) {
            throw new Exception("No driver class specified.");
        }
        if (!classLoaders.containsKey(libraryName)) {
            if (new File(libraryName).exists()) {
                loadDriverClasses(libraryName);
            } else {
                throw new Exception("Could not find file \"" + libraryName +"\".");
            }
        }
        ClassLoader classLoader = classLoaders.get(libraryName);
        try {
            return (Driver) Class.forName(className, true, classLoader).newInstance();
        } catch (Exception e) {
            throw new Exception(
                    "Could not forName class \"" + className + "\" " +
                    "from library \"" + libraryName + "\". " +
                    "[" + NamingUtil.getClassName(e.getClass()) + "] " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseDriverManager m = new DatabaseDriverManager();
        File file = new File("D:\\Projects\\DBNavigator\\lib\\classes12.jar");
        String[] classes = m.loadDriverClasses(file.getPath());
    }
}
