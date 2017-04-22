// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.browser;

import org.cef.handler.CefClientHandler;

/**
 * Creates a new instance of CefBrowser according the passed values
 */
public class CefBrowserFactory {
  public enum RenderType {RENDER_OPENGL, RENDER_AWT_WINDOW, RENDER_BYTE_BUFFER}

  public static CefBrowser create(CefClientHandler clientHandler,
                                  String url,
                                  boolean isTransparent,
                                  CefRequestContext context,
                                  RenderType renderType) {
    return create(clientHandler, url, isTransparent, context, renderType, 800, 600, null);
  }

  public static CefBrowser create(CefClientHandler clientHandler,
                                  String url,
                                  boolean isTransparent,
                                  CefRequestContext context,
                                  RenderType renderType,
                                  CefRenderer cefRenderer) {
    return create(clientHandler, url, isTransparent, context, renderType, 800, 600, cefRenderer);
  }

  public static CefBrowser create(CefClientHandler clientHandler,
                                  String url,
                                  boolean isTransparent,
                                  CefRequestContext context,
                                  RenderType renderType,
                                  int browserWidth,
                                  int browserHeight,
                                  CefRenderer cefRenderer) {
    if (renderType == RenderType.RENDER_OPENGL)
      return new CefRendererBrowserOpenGL(clientHandler, url, isTransparent, context);
    else if (renderType == RenderType.RENDER_AWT_WINDOW)
      return new CefRendererBrowserAWT(clientHandler, url, isTransparent, context);
    else if (renderType == RenderType.RENDER_BYTE_BUFFER)
      return new CefRendererBrowserBuffer(clientHandler, url, isTransparent, context, browserWidth, browserHeight, cefRenderer);
    else return null;
  }

}