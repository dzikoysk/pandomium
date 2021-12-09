package org.panda_lang.pandomium.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SystemUtils {
    private List<String> libs = new ArrayList<>();

    public static void injectLibraryPath(String libraryPath) throws Exception {
        System.setProperty("java.library.path", libraryPath);
        /*
        for (String lib : new SystemUtils().getLibs(new File(libraryPath))) {
            try{
                System.loadLibrary(lib);
            } catch (Throwable exception) {
                exception.printStackTrace();
            }
        }

         */

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

    public List<String> getLibs(File baseLib){
        searchLib(baseLib);
        return libs;
    }

    public void searchLib(File file) {
        if (file.isDirectory()) {
            File[] arr = file.listFiles();
            for (File f : arr) {
                searchLib(f);
            }
        } else {
            if (file.getName().endsWith(".dll") || file.getName().endsWith(".so")) {
                libs.add(FileUtils.getFileNameWithoutExt(file.getName()));
            }
        }
    }
}
