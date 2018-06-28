package org.cef;

import java.lang.reflect.InvocationTargetException;

public interface CefThreadBridge {

    void invokeLater(Runnable runnable);

    void invokeAndWait(Runnable runnable) throws InvocationTargetException, InterruptedException;

    boolean isEventDispatchThread();

}
