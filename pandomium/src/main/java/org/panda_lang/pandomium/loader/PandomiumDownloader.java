package org.panda_lang.pandomium.loader;

import org.panda_lang.framework.util.IOUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class PandomiumDownloader {

    private final PandomiumLoader loader;

    public PandomiumDownloader(PandomiumLoader loader) {
        this.loader = loader;
    }

    public InputStream download(String remotePath) {
        System.out.println("Download: " + remotePath);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream in = null;

        try {
            URL url = new URL(remotePath);
            URLConnection conn = url.openConnection();
            int size = conn.getContentLength();

            in = new BufferedInputStream(url.openStream());

            byte data[] = new byte[1024];
            double sumCount = 0.0;
            int count;

            while ((count = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, count);
                sumCount += count;

                int progress = (int) (((sumCount / size * 100.0)));
                progress -= 0.2 * progress;
                progress += 10;

                if (size > 0 && loader.getProgress() != progress) {
                    loader.updateProgress(progress);
                }
            }

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }

        return null;
    }

}
