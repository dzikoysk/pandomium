package org.panda_lang.pandomium.wrapper;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.CefThreadBridge;
import org.panda_lang.pandomium.util.os.PandomiumOS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PandomiumThread extends Thread implements CefThreadBridge {

    private final PandomiumCEF pandomiumCEF;
    private final List<Runnable> delegates;
    protected CefApp app;
    protected CefClient client;
    protected boolean healthy;

    public PandomiumThread(PandomiumCEF pandomiumCEF) {
        super("PandomiumThread");

        this.pandomiumCEF = pandomiumCEF;
        this.delegates = new CopyOnWriteArrayList<>();
        this.healthy = true;
    }

    @Override
    public void run() {
        CefApp.setThreadBridge(this);

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = PandomiumOS.isLinux();

        this.app = CefApp.getInstance(settings);
        this.client = app.createClient();

        while (isHealthy()) {
            if (delegates.isEmpty()) {
                continue;
            }

            List<Runnable> copy = new ArrayList<>(delegates);
            delegates.clear();

            for (Runnable runnable : copy) {
                runnable.run();
            }
        }
    }

    public void dispose() {
        invokeLater(() -> {
            app.dispose();
            app.addShutdownAction(() -> {
                setHealthy(false);
                interrupt();
            });
        });
    }

    @Override
    public void interrupt() {
        setHealthy(false);
        super.interrupt();
    }

    @Override
    public void invokeLater(Runnable runnable) {
        if (!isHealthy()) {
            throw new RuntimeException("PandomiumThread is not healthy");
        }

        delegates.add(runnable);
    }

    @Override
    public void invokeAndWait(Runnable runnable) {
        invokeLater(runnable);
        while (delegates.size() != 0);
    }

    @Override
    public boolean isEventDispatchThread() {
        return Thread.currentThread().getId() == this.getId();
    }

    public boolean isPrepared() {
        return getApp() != null && getClient() != null;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public CefClient getClient() {
        return client;
    }

    public CefApp getApp() {
        return app;
    }

}
