package org.panda_lang.pandomium;

import org.panda_lang.pandomium.loader.PandomiumLoader;
import org.panda_lang.pandomium.loader.PandomiumProgressListener;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumCEF;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

public class Pandomium {

    private final PandomiumSettings settings;
    private final PandomiumLoader loader;
    private PandomiumCEF pcef;

    public Pandomium(PandomiumSettings settings) {
        this.settings = settings;
        this.loader = new PandomiumLoader(this);
    }

    public void initialize() {
        loader.addProgressListener((state, progress) -> {
            if (state != PandomiumProgressListener.State.DONE) {
                return;
            }

            pcef = new PandomiumCEF(this);
            pcef.initialize();
        });

        loader.load();
    }

    public PandomiumClient createClient() {
        if (pcef == null) {
            throw new RuntimeException("Pandomium is not initialized");
        }

        return pcef.createClient();
    }

    public void dispose() {
        if (pcef != null) {
            pcef.dispose();
        }
    }

    public PandomiumCEF getRaw() {
        return pcef;
    }

    public PandomiumLoader getLoader() {
        return loader;
    }

    public PandomiumSettings getSettings() {
        return settings;
    }

    public static String getVersion() {
        return PandomiumConstants.PANDOMIUM_VERSION;
    }

}
