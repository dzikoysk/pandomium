package org.panda_lang.pandomium.win64;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowsSwingTest extends JFrame {

    private final JTextField address;
    private final CefBrowser cefBrowser;

    private WindowsSwingTest(String startURL, boolean useOSR, boolean isTransparent) {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                if (state == CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        CefApp cefApp = CefApp.getInstance(settings);
        CefClient cefClient = cefApp.createClient();

        cefBrowser = cefClient.createBrowser(startURL, useOSR, isTransparent);
        Component browserUI = cefBrowser.getUIComponent();

        address = new JTextField(startURL, 100);
        address.addActionListener(e -> cefBrowser.loadURL(address.getText()));

        getContentPane().add(address, BorderLayout.NORTH);
        getContentPane().add(browserUI, BorderLayout.CENTER);
        pack();
        setSize(800, 600);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new WindowsSwingTest("http://www.google.com", OS.isLinux(), false);
    }

}