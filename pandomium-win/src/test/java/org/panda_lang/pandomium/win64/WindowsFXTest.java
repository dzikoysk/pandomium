package org.panda_lang.pandomium.win64;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

import javax.swing.*;
import java.awt.*;

public class WindowsFXTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> initializeSwing(swingNode));

        BorderPane parent = new BorderPane();
        parent.setCenter(swingNode);

        primaryStage.setOnCloseRequest(event -> {
            CefApp.getInstance().dispose();
            System.exit(0);
        });

        Scene scene = new Scene(parent, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeSwing(SwingNode swingNode) {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                if (state == CefApp.CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = OS.isLinux();
        CefApp cefApp = CefApp.getInstance(settings);
        CefClient cefClient = cefApp.createClient();

        CefBrowser cefBrowser = cefClient.createBrowser("http://www.google.com", OS.isLinux(), false);
        Component browserUI = cefBrowser.getUIComponent();

        JPanel panel = new JPanel();
        panel.add(browserUI);

        swingNode.setContent(panel);
    }

    public static void main(String[] args) {
        Application.launch(WindowsFXTest.class, args);
    }

}
