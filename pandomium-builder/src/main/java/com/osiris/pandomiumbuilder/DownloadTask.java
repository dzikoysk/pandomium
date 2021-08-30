/*
 * Copyright Osiris Team
 * All rights reserved.
 *
 * This software is copyrighted work licensed under the terms of the
 * AutoPlug License.  Please consult the file "LICENSE" for details.
 */

package com.osiris.pandomiumbuilder;

import com.osiris.betterthread.BetterThread;
import com.osiris.betterthread.BetterThreadManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

public class DownloadTask extends BetterThread {
    private String url;
    private File dest;
    private String[] allowedSubContentTypes;

    /**
     * Downloads a file from an url to the cache first and then
     * to its final destination.
     *
     * @param name    This processes name.
     * @param manager the parent process manager.
     * @param url     the download-url.
     * @param dest    the downloads final destination.
     */
    public DownloadTask(String name, BetterThreadManager manager, String url, File dest) {
        this(name, manager, url, dest, (String[]) null);
    }

    public DownloadTask(String name, BetterThreadManager manager, String url, File dest, String... allowedSubContentTypes) {
        this(name, manager);
        this.url = url;
        this.dest = dest;
        this.allowedSubContentTypes = allowedSubContentTypes;
    }

    private DownloadTask(String name, BetterThreadManager manager) {
        super(name, manager);
    }

    @Override
    public void runAtStart() throws Exception {
        super.runAtStart();

        if (dest==null)
            dest = new File(System.getProperty("user.dir")+"/"+new File(new URL(url).getFile()).getName());
        else if(dest.isDirectory()){
            dest = new File(dest+"/"+new File(new URL(url).getFile()).getName());
        }
        // Or dest is already a file and we got no problems

        final String fileName = dest.getName();
        setStatus("Downloading " + fileName + "... (0mb/0mb)");

        Request request = new Request.Builder().url(url)
                .header("User-Agent", "Pandonium-Builder/" + new Random().nextInt() + " - https://github.com/dzikoysk/pandomium")
                .build();
        Response response = new OkHttpClient().newCall(request).execute();
        ResponseBody body = null;
        try {
            if (response.code() != 200)
                throw new Exception("Download of '" + dest.getName() + "' failed! Code: " + response.code() + " Message: " + response.message() + " Url: " + url);

            body = response.body();
            if (body == null)
                throw new Exception("Download of '" + dest.getName() + "' failed because of null response body!");
            else if (body.contentType() == null)
                throw new Exception("Download of '" + dest.getName() + "' failed because of null content type!");
            else if (!body.contentType().type().equals("application"))
                throw new Exception("Download of '" + dest.getName() + "' failed because of invalid content type: " + body.contentType().type());
            else if (!body.contentType().subtype().equals("java-archive")
                    && !body.contentType().subtype().equals("jar")
                    && !body.contentType().subtype().equals("octet-stream")) {
                if (allowedSubContentTypes == null)
                    throw new Exception("Download of '" + dest.getName() + "' failed because of invalid sub-content type: " + body.contentType().subtype());
                if (!Arrays.asList(allowedSubContentTypes).contains(body.contentType().subtype()))
                    throw new Exception("Download of '" + dest.getName() + "' failed because of invalid sub-content type: " + body.contentType().subtype());
            }

            long completeFileSize = body.contentLength();
            setMax(completeFileSize);

            BufferedInputStream in = new BufferedInputStream(body.byteStream());
            FileOutputStream fos = new FileOutputStream(dest);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                setStatus("Downloading " + fileName + "... (" + downloadedFileSize / (1024 * 1024) + "mb/" + completeFileSize / (1024 * 1024) + "mb)");
                setNow(downloadedFileSize);

                bout.write(data, 0, x);
            }

            setStatus("Downloaded " + fileName + " (" + downloadedFileSize / (1024 * 1024) + "mb/" + completeFileSize / (1024 * 1024) + "mb)");
            bout.close();
            in.close();
            body.close();
            response.close();
        } catch (Exception e) {
            if (body != null) body.close();
            response.close();
            throw e;
        }
    }

    public String getUrl() {
        return url;
    }

    public File getDest() {
        return dest;
    }

    public String[] getAllowedSubContentTypes() {
        return allowedSubContentTypes;
    }
}
