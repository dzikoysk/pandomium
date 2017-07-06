package org.panda_lang.pandomium.loader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PandomiumDownloader {

    private final PandomiumLoader loader;

    public PandomiumDownloader(PandomiumLoader loader) {
        this.loader = loader;
    }

    public InputStream download(String remotePath) {
        System.out.println("Downloading " + remotePath);

        try {
            URL url = new URL(remotePath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();

            return connection.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
