package org.panda_lang.pandomium.loader;

import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.loader.PandomiumProgressListener.State;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;

import java.util.ArrayList;
import java.util.Collection;

public class PandomiumLoader {

    private final Pandomium pandomium;
    private final Collection<PandomiumProgressListener> progressListeners;
    private int progress;

    public PandomiumLoader(Pandomium pandomium) {
        this.pandomium = pandomium;
        this.progressListeners = new ArrayList<>();
    }

    public void load() {
        PandomiumLoaderWorker worker = new PandomiumLoaderWorker(this);

        PandomiumSettings settings = pandomium.getSettings();
        LoaderSettings loaderSettings = settings.getLoader();

        progressListeners.add((state, progress) -> {
            if (state == State.RUNNING) {
                System.out.println(progress + "%");
            }
        });

        if (!loaderSettings.isLoadAsync()) {
            worker.run();
            return;
        }

        Thread loaderThread = new Thread(worker, "Pandomium Loader Thread");
        loaderThread.start();
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
