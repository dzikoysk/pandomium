package org.panda_lang.pandomium.loader;

public interface PandomiumProgressListener {

    void onUpdate(State state, int progress);

    enum State {
        RUNNING,
        DONE
    }

}
