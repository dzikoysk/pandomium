package org.panda_lang.pandomium.wrapper;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefRequestContext;

public class PandomiumClient {
    private final CefApp cefApp;
    private final CefClient cefClient;
    private boolean isOffscreenRendered;
    private boolean isTransparent;
    private CefRequestContext context;

    /**
     * Initialises a new {@link PandomiumClient} object.
     *
     * @param cefApp              the application context.
     * @param isOffscreenRendered should the browser be rendered offscreen.
     * @param isTransparent       should the browser be transparent.
     */
    public PandomiumClient(CefApp cefApp, boolean isOffscreenRendered, boolean isTransparent) {
        this.cefApp = cefApp;
        this.cefClient = cefApp.createClient();
        this.isOffscreenRendered = isOffscreenRendered;
        this.isTransparent = isTransparent;
    }

    public CefBrowser createBrowser(String url) {
        return cefClient.createBrowser(url, isOffscreenRendered, isTransparent, context = CefRequestContext.getGlobalContext());
    }

    /**
     * Same as {@link #createBrowser(String)}.
     */
    public CefBrowser loadURL(String url) {
        return createBrowser(url);
    }

    public CefClient getCefClient() {
        return cefClient;
    }

    public CefApp getCefApp() {
        return cefApp;
    }

    public CefRequestContext getContext() {
        return context;
    }

    public boolean isOffscreenRendered() {
        return isOffscreenRendered;
    }

    public void setOffscreenRendered(boolean offscreenRendered) {
        isOffscreenRendered = offscreenRendered;
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }
}
