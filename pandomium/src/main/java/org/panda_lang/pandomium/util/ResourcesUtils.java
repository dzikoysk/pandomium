package org.panda_lang.pandomium.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResourcesUtils {

    public static Collection<String> getResources(Pattern pattern) {
        ArrayList<String> resources = new ArrayList<>();
        String classPath = System.getProperty("java.class.path", ".");
        String[] classPathElements = classPath.split(System.getProperty("path.separator"));

        for (String element : classPathElements) {
            resources.addAll(getResources(element, pattern));
        }

        return resources;
    }

    private static Collection<String> getResources(String element, Pattern pattern) {
        final ArrayList<String> retval = new ArrayList<String>();
        final File file = new File(element);
        if (file.isDirectory()) {
            retval.addAll(getResourcesFromDirectory(file, pattern));
        } else {
            retval.addAll(getResourcesFromJarFile(file, pattern));
        }
        return retval;
    }

    private static Collection<String> getResourcesFromJarFile(File file, Pattern pattern) {
        ArrayList<String> resources = new ArrayList<String>();
        ZipFile zf;

        try {
            zf = new ZipFile(file);
        } catch (IOException e) {
            throw new Error(e);
        }

        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = (ZipEntry) e.nextElement();
            String fileName = ze.getName();
            boolean accept = pattern.matcher(fileName).matches();

            if (accept) {
                resources.add(fileName);
            }
        }

        try {
            zf.close();
        } catch (final IOException e1) {
            throw new Error(e1);
        }

        return resources;
    }

    private static Collection<String> getResourcesFromDirectory(File directory, Pattern pattern) {
        ArrayList<String> resources = new ArrayList<>();
        File[] fileList = directory.listFiles();

        if (fileList == null) {
            return resources;
        }

        for (File file : fileList) {
            if (file.isDirectory()) {
                resources.addAll(getResourcesFromDirectory(file, pattern));
            } else {
                try {
                    String fileName = file.getCanonicalPath();
                    boolean accept = pattern.matcher(fileName).matches();

                    if (accept) {
                        resources.add(fileName);
                    }
                } catch (final IOException e) {
                    throw new Error(e);
                }
            }
        }

        return resources;
    }

}