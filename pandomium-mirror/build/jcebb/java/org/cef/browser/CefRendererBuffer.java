// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.browser;

import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

class CefRendererBuffer implements CefRenderer {
  private boolean transparent_;
  private int view_width_ = 0;
  private int view_height_ = 0;
  private boolean use_draw_pixels_ = false;

  protected CefRendererBuffer(boolean transparent) {
    transparent_ = transparent;
  }

  protected boolean isTransparent() {
    return transparent_;
  }

  @Override
  public void render() {
    System.out.println("Render");
  }

  @Override
  public void onPaint(boolean popup,
                         Rectangle[] dirtyRects,
                         ByteBuffer buffer,
                         int width,
                         int height) {
    System.out.println("Paint");
  }
}
