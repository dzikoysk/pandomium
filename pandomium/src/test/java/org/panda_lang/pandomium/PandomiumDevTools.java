package org.panda_lang.pandomium;

import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class PandomiumDevTools {

    public static void main(String[] args) {
        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder()
                .build();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.loadURL("http://google.com");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> init(frame, "Pandomium"));

        JFrame devToolsFrame = new JFrame();
        devToolsFrame.getContentPane().add(browser.getCefBrowser().getDevTools().getUIComponent(), BorderLayout.CENTER);
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
