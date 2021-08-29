package org.panda_lang.pandomium.settings;

import net.dzikoysk.dynamiclogger.Logger;
import org.cef.CefSettings;
import org.panda_lang.pandomium.settings.categories.CommandLineSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.UtilsOS;

public class PandomiumSettings {

    private final Logger logger;
    private final CommandLineSettings commandLine;
    private final NativesSettings natives;
    private final LoaderSettings loader;
    private final CefSettings cef;

    protected PandomiumSettings(Logger logger, CommandLineSettings commandLine, NativesSettings natives, LoaderSettings loader) {
        this.logger = logger;
        this.commandLine = commandLine;
        this.natives = natives;
        this.loader = loader;

        this.cef = new CefSettings();
        this.cef.windowless_rendering_enabled = UtilsOS.isLinux();
    }

    public static PandomiumSettingsBuilder builder() {
        return new PandomiumSettingsBuilder();
    }

    public static PandomiumSettingsBuilder getDefaultSettingsBuilder() {
        return PandomiumSettings.builder()
                .nativeDirectory("natives")
                .loadAsync(false);
    }

    public static PandomiumSettings getDefaultSettings() {
        return getDefaultSettingsBuilder().build();
    }

    public LoaderSettings getLoader() {
        return loader;
    }

    public NativesSettings getNatives() {
        return natives;
    }

    public CommandLineSettings getCommandLine() {
        return commandLine;
    }

    public CefSettings getCefSettings() {
        return cef;
    }

    public Logger getLogger() {
        return logger;
    }

}
