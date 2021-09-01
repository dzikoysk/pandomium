package org.panda_lang.pandomium.wrapper;

import org.cef.CefApp;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;

public class PandomiumCEF {

    private final Pandomium pandomium;
    private CefApp cefApp;

    public PandomiumCEF(Pandomium pandomium) {
        this.pandomium = pandomium;
    }

    public void initialize() {
        PandomiumSettings settings = pandomium.getSettings();
        this.cefApp = CefApp.getInstance(settings.getCommandLine().getArguments(), settings.getCefSettings());
    }

    public PandomiumClient createClient() {
        return new PandomiumClient(this, this.cefApp.createClient());
    }

    public CefApp getCefApp() {
        return cefApp;
    }

}
