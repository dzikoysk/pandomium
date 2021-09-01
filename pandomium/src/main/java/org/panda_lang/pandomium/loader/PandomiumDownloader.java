package org.panda_lang.pandomium.loader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class PandomiumDownloader {

    private final Collection<HttpURLConnection> connections;

    public PandomiumDownloader(PandomiumLoader loader) {
        this.connections = new ArrayList<>(1);
    }

    protected static long getFileSize(URL url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        long length = conn.getContentLengthLong();
        conn.disconnect();

        return length;
    }

    public InputStream download(URL url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connections.add(connection);

        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        return connection.getInputStream();
    }

    public void closeConnections() {
        for (HttpURLConnection connection : connections) {
            try {
                connection.disconnect();
            } catch (Exception e) {
                // mute
            }
        }

        connections.clear();
    }

}
