package org.panda_lang.pandomium;

import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PandomiumTest {

    public static void main(String[] args) {
        PandomiumSettings settings = PandomiumSettings.getDefaultSettings();

        Pandomium pandomium = new Pandomium(settings);
        pandomium.initialize();

        PandomiumClient client = pandomium.createClient();
        PandomiumBrowser browser = client.loadURL("https://panda-lang.org");

        JFrame frame = new JFrame();
        frame.getContentPane().add(browser.toAWTComponent(), BorderLayout.CENTER);
        frame.pack();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pandomium.dispose();
                frame.dispose();
            }
        });

        frame.setTitle("Pandomium");
        frame.setSize(1380, 760);
        frame.setVisible(true);
    }

}
