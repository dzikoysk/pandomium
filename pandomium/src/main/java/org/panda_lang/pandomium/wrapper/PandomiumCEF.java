package org.panda_lang.pandomium.wrapper;

import org.cef.CefApp;
import org.cef.CefSettings;
import org.cef.handler.CefAppHandlerAdapter;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.util.os.PandomiumOS;

public class PandomiumCEF {

    private final Pandomium pandomium;
    private CefApp cefApp;

    public PandomiumCEF(Pandomium pandomium) {
        this.pandomium = pandomium;
    }

    public void initialize() {
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = PandomiumOS.isLinux();

        this.cefApp = CefApp.getInstance(settings);

        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    pandomium.exit();
                }
            }
        });
    }

    public void dispose() {
        cefApp.dispose();
    }

    public CefApp getCefApp() {
        return cefApp;
    }

}
