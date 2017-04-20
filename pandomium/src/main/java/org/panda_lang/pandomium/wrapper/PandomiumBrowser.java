package org.panda_lang.pandomium.wrapper;

import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;

public class PandomiumBrowser {

    private final CefBrowser cefBrowser;

    public PandomiumBrowser(CefBrowser cefBrowser) {
        this.cefBrowser = cefBrowser;
    }

    public JPanel toSwingComponent() {
        JPanel panel = new JPanel();
        panel.add(toAWTComponent());
        return panel;
    }

    public Component toAWTComponent() {
        return cefBrowser.getUIComponent();
    }

    public CefBrowser getCefBrowser() {
        return cefBrowser;
    }

}
