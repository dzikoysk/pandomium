package org.panda_lang.pandomium;

import net.dzikoysk.dynamiclogger.Journalist;
import net.dzikoysk.dynamiclogger.Logger;
import org.panda_lang.pandomium.loader.PandomiumLoader;
import org.panda_lang.pandomium.loader.PandomiumProgressListener;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumCEF;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import java.awt.*;

public class Pandomium implements Journalist {

    private final PandomiumSettings settings;
    private final PandomiumLoader loader;
    private final Thread mainThread;
    private PandomiumCEF pcef;

    public Pandomium(PandomiumSettings settings) {
        this.settings = settings;
        this.loader = new PandomiumLoader(this);
        this.mainThread = Thread.currentThread();
    }

    public static String getChromiumVersion() {
        return PandomiumConstants.PANDOMIUM_CHROMIUM_VERSION;
    }

    public static String getCefVersion() {
        return PandomiumConstants.PANDOMIUM_CEF_VERSION;
    }

    public static String getVersion() {
        return PandomiumConstants.PANDOMIUM_VERSION;
    }

    public void initialize() {
        Toolkit.getDefaultToolkit();

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

    public PandomiumCEF getRaw() {
        return pcef;
    }

    public PandomiumLoader getLoader() {
        return loader;
    }

    public PandomiumSettings getSettings() {
        return settings;
    }

    @Override
    public Logger getLogger() {
        return settings.getLogger();
    }

}
