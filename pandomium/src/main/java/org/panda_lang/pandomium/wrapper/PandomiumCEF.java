package org.panda_lang.pandomium.wrapper;

import org.cef.*;
import org.panda_lang.pandomium.*;

public class PandomiumCEF {

    private final Pandomium pandomium;
    private CefApp cefApp;

    public PandomiumCEF(Pandomium pandomium) {
        this.pandomium = pandomium;
    }

    public void initialize() {
        this.cefApp = CefApp.getInstance(pandomium.getSettings().getCefSettings());
    }

    public PandomiumClient createClient() {
        return new PandomiumClient(this,  this.cefApp.createClient());
    }

}
