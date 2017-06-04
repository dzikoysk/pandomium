package org.panda_lang.pandomium.wrapper;

import org.panda_lang.pandomium.Pandomium;

public class PandomiumCEF {

    private final Pandomium pandomium;
    private final PandomiumThread pandomiumThread;

    public PandomiumCEF(Pandomium pandomium) {
        this.pandomium = pandomium;
        this.pandomiumThread = new PandomiumThread(this);
    }

    public void initialize() {
        pandomiumThread.start();

        while (!pandomiumThread.isPrepared()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public PandomiumClient createClient() {
        return new PandomiumClient(this, pandomiumThread.getClient());
    }

    public void dispose() {
        pandomiumThread.dispose();
    }

    public PandomiumThread getPandomiumThread() {
        return pandomiumThread;
    }

}
