package org.panda_lang.pandomium.loader;

public interface PandomiumProgressListener {

    enum State {
        RUNNING,
        DONE
    }

    void onUpdate(State state, int progress);

}
