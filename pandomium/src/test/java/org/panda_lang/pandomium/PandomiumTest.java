package org.panda_lang.pandomium;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.util.os.PandomiumOS;
import org.panda_lang.pandomium.wrapper.PandomiumCEF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PandomiumTest {

    public static void main(String[] args) {
        PandomiumSettings settings = PandomiumSettings.getDefaultSettings();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumCEF pcef = pandomium.getRaw();
        CefApp app = pcef.getCefApp();

        CefClient client = app.createClient();
        CefBrowser browser = client.createBrowser("https://panda-lang.org", PandomiumOS.isLinux(), false);
        Component browserComponent = browser.getUIComponent();

        JFrame frame = new JFrame();
        frame.getContentPane().add(browserComponent, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                pandomium.exit();
            }
        });

        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

}
