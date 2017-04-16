package org.panda_lang.pandomium.os;

public class PandomiumOS {

    private static final PandomiumOSType OS = getOSType();

    public static boolean isLinux() {
        return getOSType() == PandomiumOSType.OS_LINUX;
    }

    public static boolean isWindows() {
        return getOSType() == PandomiumOSType.OS_WINDOWS;
    }

    public static boolean isMacintosh() {
        return getOSType() == PandomiumOSType.OS_MACINTOSH;
    }

    private static PandomiumOSType getOSType() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.startsWith("windows")) {
            return PandomiumOSType.OS_WINDOWS;
        }
        else if (os.startsWith("linux")) {
            return PandomiumOSType.OS_LINUX;
        }
        else if (os.startsWith("mac")) {
            return PandomiumOSType.OS_MACINTOSH;
        }

        return PandomiumOSType.UNKNOWN;
    }
}
