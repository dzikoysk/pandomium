package org.panda_lang.pandomium.loader;

import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.settings.categories.DependenciesSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.ArchiveUtils;
import org.panda_lang.pandomium.util.FileUtils;
import org.panda_lang.pandomium.util.os.PandomiumOS;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class PandomiumNativesLoader {

    protected void loadNatives(PandomiumLoader loader) throws Exception {
        Pandomium pandomium = loader.getPandomium();
        PandomiumSettings settings = pandomium.getSettings();

        NativesSettings nativesSettings = settings.getNatives();
        File nativesDirectory = new File(nativesSettings.getNativeDirectory());

        if (checkNative(nativesDirectory)) {
            return;
        }
        loader.updateProgress(5);

        FileUtils.handleFileResult(FileUtils.delete(nativesDirectory), "Couldn't delete directory %s", nativesDirectory.getAbsolutePath());
        FileUtils.handleFileResult(nativesDirectory.mkdirs(), "Couldn't create directory %s", nativesDirectory.getAbsolutePath());
        loader.updateProgress(10);

        DependenciesSettings dependenciesSettings = settings.getDependencies();
        URL dependenciesURL = new URL(dependenciesSettings.getPlatformURL());

        long contentLength = PandomiumDownloader.getFileSize(dependenciesURL);
        pandomium.getLogger().info("Starting to download " + FileUtils.convertBytes(contentLength) + " of data");

        pandomium.getLogger().info("Downloading " + dependenciesSettings.getPlatformURL());
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

        if (PandomiumOS.isWindows()) {
            success = success && FileUtils.isIn("chrome_elf.dll", directoryContent) && FileUtils.isIn("jcef.dll", directoryContent);
        }
        else if (PandomiumOS.isLinux()) {
            success = success && FileUtils.isIn("cef.pak", directoryContent);
        }
        
        // Ensure that 'jcef helper' is executable
        String cefHelperName = null;

        if (PandomiumOS.isMacOS()) {
            cefHelperName = "jcef Helper";
        }
        else if (PandomiumOS.isWindows()) {
            cefHelperName = "jcef_helper.exe";
        }
        else if (PandomiumOS.isLinux()) {
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
