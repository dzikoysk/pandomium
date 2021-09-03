package org.panda_lang.pandomium;

import org.cef.browser.CefBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class PandomiumDevTools {

    public static void main(String[] args) {
        Pandomium pandomium = Pandomium.buildDefault();

        PandomiumClient client = pandomium.createClient();
        CefBrowser browser = client.loadURL("http://google.com");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.getUIComponent(), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> init(frame, "Pandomium"));

        JFrame devToolsFrame = new JFrame();
        devToolsFrame.getContentPane().add(browser.getDevTools().getUIComponent(), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> init(devToolsFrame, "Pandomium DevTools"));
    }

    private static void init(JFrame frame, String title) {
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

        frame.setTitle(title);
        frame.setSize(1720, 840);
        frame.setVisible(true);
    }

}
