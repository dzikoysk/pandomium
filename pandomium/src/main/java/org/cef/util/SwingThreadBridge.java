package org.cef.util;

import org.cef.CefThreadBridge;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class SwingThreadBridge implements CefThreadBridge {

    @Override
    public void invokeLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public void invokeAndWait(Runnable runnable) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(runnable);
    }

    @Override
    public boolean isEventDispatchThread() {
        return SwingUtilities.isEventDispatchThread();
    }

}
