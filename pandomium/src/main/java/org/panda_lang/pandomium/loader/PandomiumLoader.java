package org.panda_lang.pandomium.loader;


import net.dzikoysk.dynamiclogger.Logger;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.loader.PandomiumProgressListener.State;
import org.panda_lang.pandomium.loader.os.PandomiumLinuxNativesLoader;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.util.JarUtils;
import org.panda_lang.pandomium.util.OSUtils;
import org.panda_lang.pandomium.util.SystemUtils;

import java.util.ArrayList;
import java.util.Collection;

public class PandomiumLoader {

    private final Pandomium pandomium;
    private final Logger log;
    private final Collection<PandomiumProgressListener> progressListeners;
    private int progress;

    public PandomiumLoader(Pandomium pandomium) {
        this.pandomium = pandomium;
        this.log = pandomium.getLogger();
        this.progressListeners = new ArrayList<>();
    }

    public void load() {

        String version = null;
        try {
            version = new JarUtils().getVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("# ");
        log.info("# Launching Pandonium");
        log.info("# " + version);
        log.info("# Determined '" + OSUtils.OS_ARCH.name() + "' as operating systems architecture.");
        log.info("# Determined '" + OSUtils.OS_TYPE.name() + "' as operating system.");
        log.info("# ");

        LoaderSettings loaderSettings = pandomium.getSettings().getLoader();

        progressListeners.add((state, progress) -> {
            if (state == State.RUNNING) {
                log.info("Progress: " + progress + "%");
            }
        });


        Runnable runnable = () -> {
            try {
                this.updateProgress(0);

                PandomiumNativesLoader nativeLoader = new PandomiumNativesLoader();
                nativeLoader.loadNatives(this);

                Pandomium pandomium = this.getPandomium();
                String nativePath = pandomium.getSettings().getNatives().getNativeDirectory().getAbsolutePath();
                SystemUtils.injectLibraryPath(nativePath);

                if (OSUtils.isLinux()) {
                    PandomiumLinuxNativesLoader linuxNativesLoader = new PandomiumLinuxNativesLoader();
                    linuxNativesLoader.loadLinuxNatives(pandomium, nativePath);
                }

                this.updateProgress(100);
                this.callListeners(PandomiumProgressListener.State.DONE);
            } catch (Exception e) {
                log.error("Failed to install JCEF natives update. Message: " + e.getMessage());
                e.printStackTrace();
            }
        };

        if (loaderSettings.isLoadAsync())
            new Thread(runnable, "Pandomium Loader Thread").start();
        else
            runnable.run();

    }

    protected void updateProgress(int newProgress) {
        this.progress = newProgress;
        callListeners(PandomiumProgressListener.State.RUNNING);
    }

    protected void callListeners(PandomiumProgressListener.State state) {
        for (PandomiumProgressListener listener : progressListeners) {
            listener.onUpdate(state, progress);
        }
    }

    public void addProgressListener(PandomiumProgressListener listener) {
        progressListeners.add(listener);
    }

    public Collection<PandomiumProgressListener> getProgressListeners() {
        return progressListeners;
    }

    public int getProgress() {
        return progress;
    }

    public Pandomium getPandomium() {
        return pandomium;
    }

}
