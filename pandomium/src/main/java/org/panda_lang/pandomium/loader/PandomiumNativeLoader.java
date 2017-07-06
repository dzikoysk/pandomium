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

public class PandomiumNativeLoader {

    @SuppressWarnings({ "unchecked" })
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
        PandomiumDownloader downloader = new PandomiumDownloader(loader);
        InputStream downloadedStream = downloader.download(dependenciesSettings.getPlatformURL());
        loader.updateProgress(91);

        downloadedStream = ArchiveUtils.unGzip(downloadedStream);
        loader.updateProgress(95);

        ArchiveUtils.unpackTar(downloadedStream, nativesDirectory);
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
        boolean success = FileUtils.isIn("jcef.so", directoryContent) || FileUtils.isIn("jcef.dll", directoryContent);

        if (PandomiumOS.isWindows()) {
            success = success && FileUtils.isIn("chrome_elf.dll", directoryContent) && FileUtils.isIn("libcef.dll", directoryContent);
        }
        else if (PandomiumOS.isLinux()) {
            success = success && FileUtils.isIn("cef.pak", directoryContent);
        }

        return success;
    }

}
