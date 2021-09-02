package org.panda_lang.pandomium;

import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PandomiumTest {

    public static void main(String[] args) {
        PandomiumClient client = Pandomium.get().createClient();
        CefBrowser browser = client.loadURL("http://google.com");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.getUIComponent(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        frame.setTitle("Pandomium");
        frame.setSize(1720, 840);
        frame.setVisible(true);

        // For background tasks:
        PandomiumClient offscreenClient = Pandomium.get().createOffscreenClient();
        offscreenClient.loadURL("http://google.com");
    }

}
