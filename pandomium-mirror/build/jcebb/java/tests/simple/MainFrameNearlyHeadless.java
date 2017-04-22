// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package tests.simple;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefBrowserFactory;
import org.cef.browser.CefRenderer;

/**
 * Trying to get this to be headless but currently it requires that a JFrame is created.
 * Maybe the C++ native code requires the Window.
 */
public class MainFrameNearlyHeadless extends JFrame {
  private static final long serialVersionUID = -5570653778104413836L;
  //private final JTextField address_;
  private final CefApp     cefApp_;
  private final CefClient  client_;
  private final CefBrowser browser_;
  private final Component  browserUI_;

  private MainFrameNearlyHeadless(String startURL, boolean isTransparent) {
    cefApp_ = CefApp.getInstance();
    client_ = cefApp_.createClient();
    CefRenderer cefRenderer = new CefRenderer() {
      @Override
      public void render() {
        System.out.println("render");
      }
      @Override
      public void onPaint(boolean popup, Rectangle[] dirtyRects, ByteBuffer buffer, int width, int height) {
        System.out.println("Painting rectangles " + dirtyRects.length);
      }
    };
    browser_ = client_.createBrowser(startURL, isTransparent, CefBrowserFactory.RenderType.RENDER_BYTE_BUFFER, null, 800, 600, cefRenderer);
    browserUI_ = browser_.getUIComponent();
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
        cefApp_.dispose();
      }
    });
  }
  public static void main(String[] args) {
    new MainFrameNearlyHeadless("http://www.google.com", true);
  }
}
