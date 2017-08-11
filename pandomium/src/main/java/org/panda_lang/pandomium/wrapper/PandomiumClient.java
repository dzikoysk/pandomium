package org.panda_lang.pandomium.wrapper;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.util.os.PandomiumOS;

public class PandomiumClient {

    private final PandomiumCEF pandomiumCEF;
    private final CefClient cefClient;

    public PandomiumClient(PandomiumCEF pandomiumCEF, CefClient cefClient) {
        this.pandomiumCEF = pandomiumCEF;
        this.cefClient = cefClient;
    }

    public PandomiumBrowser loadURL(String url) {
        return createBrowser(url);
    }

    protected PandomiumBrowser loadContent(String content) {
        return null; // TODO
    }

    private PandomiumBrowser createBrowser(String url) {
        CefBrowser browser = cefClient.createBrowser(url, PandomiumOS.isLinux(), PandomiumOS.isLinux());
        return new PandomiumBrowser(browser);
    }

    public CefClient getCefClient() {
        return cefClient;
    }

    public PandomiumCEF getPandomiumCEF() {
        return pandomiumCEF;
    }

}
