package org.panda_lang.pandomium.loader;

import com.google.gson.JsonElement;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PandomiumNativesLoader {

    protected void loadNatives(PandomiumLoader loader) throws Exception {
        Pandomium pandomium = loader.getPandomium();
        PandomiumSettings settings = pandomium.getSettings();

        NativesSettings nativesSettings = settings.getNatives();
        File nativesDirectory = nativesSettings.getNativeDirectory();

        if (checkNative(nativesDirectory)) {
            return;
        }

        loader.updateProgress(5);

        FileUtils.handleFileResult(FileUtils.delete(nativesDirectory), "Couldn't delete directory %s", nativesDirectory.getAbsolutePath());
        FileUtils.handleFileResult(nativesDirectory.mkdirs(), "Couldn't create directory %s", nativesDirectory.getAbsolutePath());
        loader.updateProgress(10);

        // Fetch download url or user custom download url for natives
        if (nativesSettings.getDownloadURL() == null) {
            String ownerAndRepo = "dzikoysk/pandomium";
            String version = new JarUtils().getVersion();
            List<String> downloadURLS = new ArrayList<>();
            for (JsonElement element :
                    new JsonUtils()
                            .getJsonObject("https://api.github.com/repos/" + ownerAndRepo + "/releases/tags/" + version)
                            .getAsJsonArray("assets")) {
                String url = element.getAsJsonObject().get("browser_download_url").getAsString();
                if (url.endsWith(".zip"))
                    downloadURLS.add(url);
            }

            // Determine the right download url for this system
            String downloadURL = null;
            for (String url :
                    downloadURLS) {
                String fileName = new File(url).getName();
                for (String os :
                        OSUtils.OS_TYPE.getAliases()) {
                    if (fileName.contains(os))
                        for (String arch :
                                OSUtils.OS_ARCH.getAliases()) {
                            if (fileName.contains(arch)) {
                                downloadURL = url;
                                break;
                            }
                        }
                }
            }

            if (downloadURL == null)
                throw new Exception("Failed to find JCEF natives download url for this system.");
            nativesSettings.setDownloadURL(downloadURL);
        }

        Objects.requireNonNull(nativesSettings.getDownloadURL());
        URL dependenciesURL = new URL(nativesSettings.getDownloadURL());

        long contentLength = PandomiumDownloader.getFileSize(dependenciesURL);
        pandomium.getLogger().info("Starting to download " + FileUtils.convertBytes(contentLength) + " of data");

        pandomium.getLogger().info("Downloading " + nativesSettings.getDownloadURL());
        PandomiumDownloader downloader = new PandomiumDownloader(loader);
        InputStream downloadedStream = downloader.download(dependenciesURL);
        loader.updateProgress(91);

        pandomium.getLogger().info("Unzipping .xz archive");
        downloadedStream = ArchiveUtils.unGzip(downloadedStream);
        loader.updateProgress(95);

        pandomium.getLogger().info("Unpacking .tar archive (it can take a while)");
        ArchiveUtils.unpackTar(downloadedStream, nativesDirectory);
        loader.updateProgress(98);

        pandomium.getLogger().info("Close connections");
        downloader.closeConnections();
        loader.updateProgress(99);
    }

    private boolean checkNative(File directory) {
        if (!directory.exists()) {
            return false;
        }

        if (!directory.isDirectory()) {
            FileUtils.handleFileResult(directory.delete(), "Couldn't delete directory %s", directory.getAbsolutePath());
            return false;
        }

        File[] directoryContent = directory.listFiles();
        boolean success = FileUtils.isIn("libcef.so", directoryContent) || FileUtils.isIn("libcef.dll", directoryContent);

        if (OSUtils.isWindows()) {
            success = success && FileUtils.isIn("chrome_elf.dll", directoryContent) && FileUtils.isIn("jcef.dll", directoryContent);
        } else if (OSUtils.isLinux()) {
            success = success && FileUtils.isIn("cef.pak", directoryContent);
        }

        // Ensure that 'jcef helper' is executable
        String cefHelperName = null;

        if (OSUtils.isMac()) {
            cefHelperName = "jcef Helper";
        } else if (OSUtils.isWindows()) {
            cefHelperName = "jcef_helper.exe";
        } else if (OSUtils.isLinux()) {
            cefHelperName = "jcef_helper";
        }

        if (cefHelperName != null && directoryContent != null) {
            for (File file : directoryContent) {
                if (file.getName().equals(cefHelperName)) {
                    file.setExecutable(true);
                    break;
                }
            }
        }

        return success;
    }

}
