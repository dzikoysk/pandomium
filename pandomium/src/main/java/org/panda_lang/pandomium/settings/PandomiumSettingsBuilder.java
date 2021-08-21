package org.panda_lang.pandomium.settings;

import net.dzikoysk.dynamiclogger.Logger;
import net.dzikoysk.dynamiclogger.backend.PrintStreamLogger;
import org.panda_lang.pandomium.settings.categories.CommandLineSettings;
import org.panda_lang.pandomium.settings.categories.DependenciesSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.os.PandomiumOSType;

import java.io.File;

public class PandomiumSettingsBuilder {

    private Logger logger = new PrintStreamLogger(System.out, System.err);
    private final CommandLineSettings commandLineSettings = new CommandLineSettings();
    private final DependenciesSettings dependenciesSettings = new DependenciesSettings();
    private final NativesSettings nativesSettings = new NativesSettings();
    private final LoaderSettings loaderSettings = new LoaderSettings();

    public PandomiumSettingsBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public PandomiumSettingsBuilder proxy(String hostname, int port) {
        commandLineSettings.addArgument("--proxy-server=" + hostname + ":" + port);
        return this;
    }

    public PandomiumSettingsBuilder argument(String argument) {
        commandLineSettings.addArgument(argument);
        return this;
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
        return new PandomiumSettings(logger, commandLineSettings, dependenciesSettings, nativesSettings, loaderSettings);
    }

}
