package org.panda_lang.pandomium.util;

import java.lang.reflect.Field;

public class SystemUtils {

    public static void injectLibraryPath(String libraryPath) throws Exception {
        System.setProperty("java.library.path", libraryPath);

        Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

}
