package org.panda_lang.pandomium;

import com.reposilite.journalist.Logger;
import com.reposilite.journalist.backend.PrintStreamLogger;
import org.cef.CefSettings;
import org.panda_lang.pandomium.settings.categories.CommandLineSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;

import java.io.File;
import java.util.function.Supplier;

public class PandomiumBuilder {

    private Logger logger = new PrintStreamLogger(System.out, System.err);
    private final CommandLineSettings commandLineSettings = new CommandLineSettings();
    private final NativesSettings nativesSettings = new NativesSettings();
    private Supplier<CefSettings> cefSettingsSupplier = CefSettings::new;

    public PandomiumBuilder logger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public PandomiumBuilder proxy(String hostname, int port) {
        commandLineSettings.addArgument("--proxy-server=" + hostname + ":" + port);
        return this;
    }

    public PandomiumBuilder argument(String argument) {
        commandLineSettings.addArgument(argument);
        return this;
    }

    public PandomiumBuilder nativeDirectory(String nativeDirectory) {
        File file = new File(nativeDirectory);

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("Cannot create native directory");
            }
        }

        nativesSettings.setNativeDirectory(file.getAbsolutePath());
        return this;
    }

    public void cefSettings(Supplier<CefSettings> cefSettingsSupplier) {
        this.cefSettingsSupplier = cefSettingsSupplier;
    }

    public Pandomium build() {
        return new Pandomium(logger, commandLineSettings, nativesSettings, cefSettingsSupplier.get());
    }

}
