package org.panda_lang.pandomium.settings;

import org.panda_lang.pandomium.PandomiumConstants;
import org.panda_lang.pandomium.settings.categories.DependenciesSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.os.PandomiumOSType;

public class PandomiumSettings {

    private final DependenciesSettings dependencies;
    private final NativesSettings natives;
    private final LoaderSettings loader;

    protected PandomiumSettings(DependenciesSettings dependencies, NativesSettings natives, LoaderSettings loader) {
        this.dependencies = dependencies;
        this.natives = natives;
        this.loader = loader;
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

    public static PandomiumSettings getDefaultSettings() {
        return PandomiumSettings.builder()
                .dependencyURL(PandomiumOSType.OS_WINDOWS, PandomiumConstants.PANDOMIUM_NATIVES_REPOSITORY + "win64-native.tar.xz")
                .dependencyURL(PandomiumOSType.OS_MAC, PandomiumConstants.PANDOMIUM_NATIVES_REPOSITORY + "mac64-native.tar.xz")
                .dependencyURL(PandomiumOSType.OS_LINUX, PandomiumConstants.PANDOMIUM_NATIVES_REPOSITORY + "linux64-native.tar.xz")
                .loadAsync(false)
                .nativeDirectory("native")
                .build();
    }

    public static PandomiumSettingsBuilder builder() {
        return new PandomiumSettingsBuilder();
    }

}
