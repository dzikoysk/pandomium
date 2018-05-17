package org.panda_lang.pandomium.loader;

import java.io.*;
import java.net.*;
import java.util.*;

public class PandomiumDownloader {

    private final PandomiumLoader loader;
    private Collection<HttpURLConnection> connections;

    public PandomiumDownloader(PandomiumLoader loader) {
        this.loader = loader;
        this.connections = new ArrayList<>(1);
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
            }
            catch (Exception e) {
                // mute
            }
        }

        connections.clear();
    }

    protected static String toHumanFormat(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    protected static long getFileSize(URL url) throws Exception {
        HttpURLConnection  conn = (HttpURLConnection) url.openConnection();

        long length = conn.getContentLengthLong();
        conn.disconnect();

        return length;
    }

}
