package org.panda_lang.pandomium.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Osiris-Team
 */
public class JarUtils {

    public String getVersion() throws Exception {
        return getThisJarsPandoniumProperties().getProperty("version");
    }

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
        if (file.exists()) {
            Collection<URL> urls = new ArrayList<URL>();
            urls.add(file.toURI().toURL());
            URLClassLoader fileClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

            java.io.InputStream is = fileClassLoader.getResourceAsStream(propertiesFileName + ".properties");
            Properties p = new Properties();
            p.load(is);
            return p;
        } else
            throw new Exception("Couldn't find the properties file at: " + path);
    }

}
