package org.panda_lang.pandomium.settings;

import org.cef.*;
import org.panda_lang.pandomium.*;
import org.panda_lang.pandomium.settings.categories.*;
import org.panda_lang.pandomium.util.os.*;

public class PandomiumSettings {

    private final CommandLineSettings commandLine;
    private final DependenciesSettings dependencies;
    private final NativesSettings natives;
    private final LoaderSettings loader;
    private final CefSettings cef;

    protected PandomiumSettings(CommandLineSettings commandLine, DependenciesSettings dependencies, NativesSettings natives, LoaderSettings loader) {
        this.commandLine = commandLine;
        this.dependencies = dependencies;
        this.natives = natives;
        this.loader = loader;

        this.cef = new CefSettings();
        this.cef.windowless_rendering_enabled = PandomiumOS.isLinux();
    }

    public LoaderSettings getLoader() {
        return loader;
    }

    public NativesSettings getNatives() {
        return natives;
    }

    public DependenciesSettings getDependencies() {
        return dependencies;
    }

    public CommandLineSettings getCommandLine() {
        return commandLine;
    }

    public CefSettings getCefSettings() {
        return cef;
    }

    public static PandomiumSettingsBuilder builder() {
        return new PandomiumSettingsBuilder();
    }

    public static PandomiumSettingsBuilder getDefaultSettingsBuilder() {
        return PandomiumSettings.builder()
                .dependencyURL(PandomiumOSType.OS_WINDOWS, PandomiumConstants.Repository.NATIVES_URL + "win64-natives.tar.xz")
                .dependencyURL(PandomiumOSType.OS_MAC, PandomiumConstants.Repository.NATIVES_URL + "mac64-natives.tar.xz")
                .dependencyURL(PandomiumOSType.OS_LINUX, PandomiumConstants.Repository.NATIVES_URL + "linux64-natives.tar.xz")
                .nativeDirectory("natives")
                .loadAsync(false);
    }

    public static PandomiumSettings getDefaultSettings() {
        return getDefaultSettingsBuilder().build();
    }

}
