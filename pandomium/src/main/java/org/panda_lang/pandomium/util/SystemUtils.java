package org.panda_lang.pandomium.util;

import java.lang.reflect.Field;

public class SystemUtils {

    public static void injectLibraryPath(String libraryPath) throws Exception {
        System.setProperty("java.library.path", libraryPath);

        try {
            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (final NoSuchFieldException e) {
            /*
             * ignore, happens on ibm-jdk-8 and adoptopenjdk-8-openj9 but has no influence since there is no cached
             * field that needs a reset
             */
        }
    }

}
