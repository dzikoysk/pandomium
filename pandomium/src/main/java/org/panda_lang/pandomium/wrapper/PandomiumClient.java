package org.panda_lang.pandomium.wrapper;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.util.os.PandomiumOS;

public class PandomiumClient {

    private final CefClient cefClient;

    public PandomiumClient(CefClient cefClient) {
        this.cefClient = cefClient;
    }

    public PandomiumBrowser createBrowser(String url) {
        CefBrowser browser = cefClient.createBrowser(url, PandomiumOS.isLinux(), false);
        return new PandomiumBrowser(browser);
    }

    public CefClient getCefClient() {
        return cefClient;
    }

}
