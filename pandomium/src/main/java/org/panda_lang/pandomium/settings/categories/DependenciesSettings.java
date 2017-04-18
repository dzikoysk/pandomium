package org.panda_lang.pandomium.settings.categories;

import org.panda_lang.pandomium.util.os.PandomiumOS;

public class DependenciesSettings {

    private String windowsModule;
    private String macOSModule;
    private String linuxModule;

    public void setWindowsModule(String windowsModule) {
        this.windowsModule = windowsModule;
    }

    public void setMacOSModule(String macOSModule) {
        this.macOSModule = macOSModule;
    }

    public void setLinuxModule(String linuxModule) {
        this.linuxModule = linuxModule;
    }

    public String getPlatformURL() {
        switch (PandomiumOS.getOS()) {
            case OS_WINDOWS:
                return getWindowsModule();
            case OS_MAC:
                return getMacOSModule();
            case OS_LINUX:
                return getLinuxModule();
            case UNKNOWN:
                throw new RuntimeException("Unsupported OS type");
        }

        return null;
    }

    public String getLinuxModule() {
        return linuxModule;
    }

    public String getMacOSModule() {
        return macOSModule;
    }

    public String getWindowsModule() {
        return windowsModule;
    }

}
