package org.panda_lang.pandomium.util;

import java.io.File;
import java.text.DecimalFormat;

public class FileUtils {

    public static String convertBytes(long bytes) {
        return (bytes == -1 ? "<unknown size> " : new DecimalFormat("#0.00").format(bytes / 1024.0 / 1024.0)) + "MB";
    }

    public static boolean delete(File f) {
        if (!f.exists()) {
            return true;
        }

        if (f.isDirectory()) {
            File[] files = f.listFiles();

            if (files != null) {
                for (File c : files) {
                    delete(c);
                }
            }
        }

        return f.delete();
    }

    public static boolean isIn(String fileName, File... files) {
        if (files == null) {
            return false;
        }

        for (File file : files) {
            if (file.getName().equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    public static void handleFileResult(boolean flag, String formattedMessage, Object... varargs) {
        if (!flag) {
            throw new IllegalStateException(String.format(formattedMessage, varargs));
        }
    }

}
