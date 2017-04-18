// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.browser;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.ByteBuffer;

import javax.media.opengl.awt.GLCanvas;

import org.cef.handler.CefClientHandler;
import org.cef.handler.CefRenderHandler;

/**
 * This class represents an off-screen rendered browser.
 */
class CefRendererBrowserBuffer extends CefBrowser_N implements CefRenderHandler {
  private CefRenderer renderer_;
  private Rectangle browser_rect_;
  private CefClientHandler clientHandler_;
  private String url_;
  private boolean isTransparent_;
  private CefRequestContext context_;
  private CefRendererBrowserBuffer parent_ = null;
  private CefRendererBrowserBuffer devTools_ = null;

  CefRendererBrowserBuffer(CefClientHandler clientHandler,
                           String url,
                           boolean transparent,
                           CefRequestContext context,
                           int browserWidth,
                           int browserHeight,
                           CefRenderer cefRenderer) {
    this(clientHandler, url, transparent, context, null, browserWidth, browserHeight, cefRenderer);
  }

  private CefRendererBrowserBuffer(CefClientHandler clientHandler,
                                   String url,
                                   boolean transparent,
                                   CefRequestContext context,
                                   CefRendererBrowserBuffer parent,
                                   int browserWidth,
                                   int browserHeight,
                                   CefRenderer cefRenderer) {
    super();

    browser_rect_ = new Rectangle(0, 0, browserWidth, browserHeight);
    isTransparent_ = transparent;

    if (cefRenderer==null)
      renderer_ = new CefRendererBuffer(transparent);
    else
      renderer_ = cefRenderer;

    clientHandler_ = clientHandler;
    url_ = url;
    context_ = context;
    parent_ = parent;

    createBrowser(clientHandler_,
      0 /* Window handle, we have no window */,
      url_,
      isTransparent_,
      null,
      context_);
  }

  @Override
  public Component getUIComponent() {
    return null;
  }

  @Override
  public CefRenderHandler getRenderHandler() {
    return this;
  }

  @Override
  public synchronized void close() {
    if (context_ != null)
      context_.dispose();
    if (parent_ != null) {
      parent_.closeDevTools();
      parent_.devTools_ = null;
      parent_ = null;
    }
    super.close();
  }

  @Override
  public synchronized CefBrowser getDevTools() {
    if (devTools_ == null) {
      devTools_ = new CefRendererBrowserBuffer(clientHandler_,
        url_,
        isTransparent_,
        context_,
        this, 0, 0, null); // @todo Does this work with a zero width and height?
    }
    return devTools_;
  }


  @Override
  public Rectangle getViewRect(CefBrowser browser) {
    return browser_rect_;
  }

  @Override
  public Point getScreenPoint(CefBrowser browser, Point viewPoint) {
    return new Point(0, 0);
  }

  @Override
  public void onPopupShow(CefBrowser browser, boolean show) {
  }

  @Override
  public void onPopupSize(CefBrowser browser, Rectangle size) {
  }

  @Override
  public void onPaint(CefBrowser browser,
                      boolean popup,
                      Rectangle[] dirtyRects,
                      ByteBuffer buffer,
                      int width,
                      int height) {
    renderer_.onPaint(popup, dirtyRects, buffer, width, height);
  }

  @Override
  public void onCursorChange(CefBrowser browser, int cursorType) {
    //canvas_.setCursor(new Cursor(cursorType));
  }
}
