package org.panda_lang.pandomium.loader;

import com.reposilite.journalist.Logger;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.loader.PandomiumProgressListener.State;
import org.panda_lang.pandomium.loader.os.PandomiumLinuxNativesLoader;
import org.panda_lang.pandomium.util.OSUtils;
import org.panda_lang.pandomium.util.SystemUtils;

import java.util.ArrayList;
import java.util.Collection;

public class PandomiumLoader {
    private final Pandomium pandomium;
    private final Logger logger;
    private final Collection<PandomiumProgressListener> progressListeners;
    private int progress;

    public PandomiumLoader(Pandomium pandomium) {
        this.pandomium = pandomium;
        this.logger = pandomium.getLogger();
        this.progressListeners = new ArrayList<>();
    }

    public void load() {
        logger.info("# ");
        logger.info("# Launching Pandonium v" + Pandomium.FULL_VERSION);
        logger.info("# Determined '" + OSUtils.OS_ARCH.name() + "' as operating systems architecture.");
        logger.info("# Determined '" + OSUtils.OS_TYPE.name() + "' as operating system.");
        logger.info("# ");

        progressListeners.add((state, progress) -> {
            if (state == State.RUNNING) {
                logger.info("Progress: " + progress + "%");
            }
        });

        try {
            this.updateProgress(0);

            PandomiumNativesLoader nativeLoader = new PandomiumNativesLoader();
            nativeLoader.loadNatives(this);

            Pandomium pandomium = this.getPandomium();
            String nativePath = pandomium.getNatives().getNativeDirectory().getAbsolutePath();
            SystemUtils.injectLibraryPath(logger, nativePath);

            if (OSUtils.isLinux()) {
                PandomiumLinuxNativesLoader linuxNativesLoader = new PandomiumLinuxNativesLoader();
                linuxNativesLoader.loadLinuxNatives(pandomium, nativePath);
            }

            this.updateProgress(100);
            this.callListeners(PandomiumProgressListener.State.DONE);
        } catch (Exception e) {
            logger.error("Failed to install JCEF natives update. Message: " + e.getMessage());
            e.printStackTrace();
        }
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
