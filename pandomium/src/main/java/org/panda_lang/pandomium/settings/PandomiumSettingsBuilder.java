package org.panda_lang.pandomium.settings;

import org.panda_lang.pandomium.settings.categories.DependenciesSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.os.PandomiumOSType;

import java.io.File;

public class PandomiumSettingsBuilder {

    private DependenciesSettings dependenciesSettings;
    private NativesSettings nativesSettings;
    private LoaderSettings loaderSettings;

    public PandomiumSettingsBuilder() {
        this.dependenciesSettings = new DependenciesSettings();
        this.nativesSettings = new NativesSettings();
        this.loaderSettings = new LoaderSettings();
    }

    public PandomiumSettingsBuilder dependencyURL(PandomiumOSType os, String url) {
        switch (os) {
            case OS_WINDOWS:
                dependenciesSettings.setWindowsModule(url);
                break;
            case OS_MAC:
                dependenciesSettings.setMacOSModule(url);
                break;
            case OS_LINUX:
                dependenciesSettings.setLinuxModule(url);
                break;
            case UNKNOWN:
                throw new RuntimeException("Unsupported OS type");
        }

        return this;
    }

    public PandomiumSettingsBuilder nativeDirectory(String nativeDirectory) {
        File file = new File(nativeDirectory);

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("Cannot create native directory");
            }
        }

        nativesSettings.setNativeDirectory(file.getAbsolutePath());
        return this;
    }

    public PandomiumSettingsBuilder loadAsync(boolean async) {
        loaderSettings.setLoadAsync(async);
        return this;
    }

    public PandomiumSettings build() {
        return new PandomiumSettings(dependenciesSettings, nativesSettings, loaderSettings);
    }

}
