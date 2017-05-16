package org.panda_lang.pandomium.wrapper.local;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PandomiumHTTPServer extends NanoHTTPD {

    private final Map<String, String> routes;
    private boolean initialized;

    public PandomiumHTTPServer() {
        super(8081);

        this.routes = new HashMap<>();
        this.initialized = false;
    }

    private void initialize() throws IOException {
        this.initialized = true;
        super.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }
    public void registerRoute(String route, String content) {
        check();
        routes.put(route, content);
    }

    private void check() {
        if (!isInitialized()) {
            try {
                initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        for (Map.Entry<String, String> route : routes.entrySet()) {
            if (!uri.contains(route.getKey())) {
                continue;
            }

            return NanoHTTPD.newFixedLengthResponse(route.getValue());
        }

        return super.serve(session);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Map<String, String> getRoutes() {
        return routes;
    }

}
