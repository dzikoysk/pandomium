package org.panda_lang.pandomium.settings;

import net.dzikoysk.dynamiclogger.Logger;
import net.dzikoysk.dynamiclogger.backend.PrintStreamLogger;
import org.panda_lang.pandomium.settings.categories.CommandLineSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;

import java.io.File;

public class PandomiumSettingsBuilder {

    private final CommandLineSettings commandLineSettings = new CommandLineSettings();
    private final NativesSettings nativesSettings = new NativesSettings();
    private final LoaderSettings loaderSettings = new LoaderSettings();
    private Logger logger = new PrintStreamLogger(System.out, System.err);

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
        return new PandomiumSettings(logger, commandLineSettings, nativesSettings, loaderSettings);
    }

}
