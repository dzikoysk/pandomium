package org.panda_lang.pandomium.util;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Osiris-Team
 */
public class JarUtils {

    @NotNull
    public Properties getThisJarsPandoniumProperties() throws Exception {
        return getPandoniumPropertiesFromJar(JarUtils.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath());
    }

    @NotNull
    public Properties getPandoniumPropertiesFromJar(@NotNull String path) throws Exception {
        return getPropertiesFromJar(path, "pandomium");
    }

    /**
     * This creates an URLClassLoader so we can access the autoplug.properties file inside the jar and then returns the properties file.
     *
     * @param path               The jars path
     * @param propertiesFileName Properties file name without its .properties extension.
     * @return autoplug.properties
     * @throws Exception
     */
    @NotNull
    public Properties getPropertiesFromJar(@NotNull String path, String propertiesFileName) throws Exception {
        File file = new File(path); // The properties file
        Properties properties = new Properties();
        if (file.exists()) {
            Collection<URL> urls = new ArrayList<URL>();
            urls.add(file.toURI().toURL());
            URLClassLoader fileClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
            java.io.InputStream is = fileClassLoader.getResourceAsStream(propertiesFileName + ".properties");
            properties.load(is);
            return properties;
        } else
            throw new Exception("Couldn't find the properties file at: " + path);
    }

    public String findVersion(File file) throws IOException {
        try (BufferedReader bw = new BufferedReader(new FileReader(file))) {
            String line;
            int startIndex;
            int stopIndex;
            while ((line = bw.readLine()) != null) {
                startIndex = line.indexOf("<version>");
                stopIndex = line.indexOf("</version>");
                if (startIndex != -1 && stopIndex != -1) {
                    return line.substring(startIndex + 9, stopIndex);
                }
            }
        }
        return null;
    }

    public File searchFileRecursively(String fileName, File file) throws IOException {
        if (file.isDirectory()) {
            File[] arr = file.listFiles();
            for (File f : arr) {
                File found = searchFileRecursively(fileName, f);
                if (found != null)
                    return found;
            }
        } else {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }


}
