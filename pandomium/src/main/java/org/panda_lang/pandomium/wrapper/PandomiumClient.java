package org.panda_lang.pandomium.wrapper;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.util.os.PandomiumOS;

public class PandomiumClient {

    private final Pandomium pandomium;
    private final CefClient cefClient;

    public PandomiumClient(Pandomium pandomium, CefClient cefClient) {
        this.pandomium = pandomium;
        this.cefClient = cefClient;
    }

    public PandomiumBrowser loadURL(String url) {
        return createBrowser(url);
    }

    public PandomiumBrowser loadContent(String content) {
        return null; // TODO
    }

    private PandomiumBrowser createBrowser(String url) {
        CefBrowser browser = cefClient.createBrowser(url, PandomiumOS.isLinux(), false);
        return new PandomiumBrowser(browser);
    }

    public CefClient getCefClient() {
        return cefClient;
    }

    public Pandomium getPandomium() {
        return pandomium;
    }

}
