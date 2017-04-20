package org.panda_lang.pandomium;

import org.cef.CefClient;
import org.panda_lang.pandomium.loader.PandomiumLoader;
import org.panda_lang.pandomium.loader.PandomiumProgressListener;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumCEF;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

public class Pandomium {

    private final PandomiumSettings settings;
    private PandomiumCEF pcef;

    public Pandomium(PandomiumSettings settings) {
        this.settings = settings;
    }

    public void initialize() {
        Pandomium that = this;
        PandomiumLoader loader = new PandomiumLoader(this);

        loader.addProgressListener((state, progress) -> {
            if (state != PandomiumProgressListener.State.DONE) {
                return;
            }

            pcef = new PandomiumCEF(that);
            pcef.initialize();
        });

        loader.load();
    }

    public PandomiumClient createClient() {
        CefClient client = pcef.getCefApp().createClient();
        return new PandomiumClient(client);
    }

    public void exit() {
        if (pcef != null) {
            pcef.dispose();
            return;
        }

        System.exit(0);
    }

    public PandomiumCEF getRaw() {
        return pcef;
    }

    public PandomiumSettings getSettings() {
        return settings;
    }

}
