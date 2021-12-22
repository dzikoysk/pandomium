package org.panda_lang.pandomium.util;

import net.dzikoysk.dynamiclogger.Journalist;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SystemUtils {
    private List<String> libs = new ArrayList<>();

    public static void injectLibraryPath(Journalist journalist, String libraryPath) throws Exception {
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
            //noinspection JavaReflectionMemberAccess
            Method initLibPaths = ClassLoader.class.getDeclaredMethod("initLibraryPaths"); // AdoptOpenJDK
            initLibPaths.setAccessible(true);
            initLibPaths.invoke(null);
        }
        catch (NoSuchMethodException | InvocationTargetException initLibraryPathsException) {
            try {
                //noinspection JavaReflectionMemberAccess
                Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths"); // General
                fieldSysPath.setAccessible(true);
                fieldSysPath.set(null, null);
            } catch (Exception sysPathsException) {
                // Unknown distribution / JDK15+
                journalist.getLogger().error("Cannot refresh library path, unsupported JDK distribution/version.");
            }
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
