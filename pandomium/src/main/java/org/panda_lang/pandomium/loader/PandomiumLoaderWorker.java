package org.panda_lang.pandomium.loader;

import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.SystemUtils;
import org.panda_lang.pandomium.util.os.PandomiumOS;
import sun.misc.Unsafe;

import java.util.HashMap;
import java.util.Map;

public class PandomiumLoaderWorker implements Runnable {

    private final PandomiumLoader loader;

    public PandomiumLoaderWorker(PandomiumLoader loader) {
        this.loader = loader;
    }

    @Override
    public void run() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() throws Exception {
        loader.updateProgress(0);

        PandomiumNativeLoader nativeLoader = new PandomiumNativeLoader();
        nativeLoader.loadNatives(loader);

        Pandomium pandomium = loader.getPandomium();
        PandomiumSettings settings = pandomium.getSettings();

        NativesSettings nativesSettings = settings.getNatives();
        SystemUtils.injectLibraryPath(nativesSettings.getNativeDirectory());

        // TODO: Modify JVM
        if (PandomiumOS.isLinux() && !System.getenv("LD_LIBRARY_PATH").contains(nativesSettings.getNativeDirectory())) {
            System.out.println("Environment variable LD_LIBRARY_PATH is not set to the \"" + nativesSettings.getNativeDirectory() +"\"");
        }

        loader.updateProgress(100);
        loader.callListeners(PandomiumProgressListener.State.DONE);
    }

}
