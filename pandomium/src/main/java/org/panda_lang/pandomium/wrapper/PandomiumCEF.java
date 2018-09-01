package org.panda_lang.pandomium.wrapper;

import org.cef.*;
import org.panda_lang.pandomium.*;
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
        return new PandomiumClient(this,  this.cefApp.createClient());
    }

}
