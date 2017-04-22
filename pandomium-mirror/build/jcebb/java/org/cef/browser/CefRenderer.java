package org.cef.browser;

import java.awt.*;
import java.nio.ByteBuffer;

public interface CefRenderer {

  public void render();

  public void onPaint(boolean popup,
                      Rectangle[] dirtyRects,
                      ByteBuffer buffer,
                      int width,
                      int height);
}

