package org.panda_lang.pandomium.loader;

import net.dzikoysk.linuxenv.*;
import org.panda_lang.pandomium.*;
import org.panda_lang.pandomium.settings.*;
import org.panda_lang.pandomium.settings.categories.*;
import org.panda_lang.pandomium.util.*;
import org.panda_lang.pandomium.util.os.*;

import java.io.*;
import java.nio.file.*;

public class PandomiumLoaderWorker implements Runnable {

    private final PandomiumLoader loader;

    public PandomiumLoaderWorker(PandomiumLoader loader) {
        this.loader = loader;
    }

    @Override
    public void run() {
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() throws Exception {
        loader.updateProgress(0);

        PandomiumNativeLoader nativeLoader = new PandomiumNativeLoader();
        nativeLoader.loadNatives(loader);

        Pandomium pandomium = loader.getPandomium();
        PandomiumSettings settings = pandomium.getSettings();

        NativesSettings nativesSettings = settings.getNatives();
        String nativePath = nativesSettings.getNativeDirectory();
        SystemUtils.injectLibraryPath(nativePath);

        if (PandomiumOS.isLinux()) {
            LinuxJVMEnvironment linuxJVMEnvironment = new LinuxJVMEnvironment();
            linuxJVMEnvironment.setJVMEnvironmentVariable("LD_LIBRARY_PATH", nativePath, 1);

            String javaHome = System.getProperty("java.home");
            File bin = new File(javaHome + File.separator + "bin");

            String[] symFiles = new String[] { "icudtl.dat", "natives_blob.bin", "snapshot_blob.bin" };
            File[] binFiles = bin.listFiles();

            for (String name : symFiles) {
                if (FileUtils.isIn(name, binFiles)) {
                    continue;
                }

                Path link =  Paths.get(bin.getAbsolutePath() + File.separator + name);
                Path target = Paths.get(nativePath + File.separator + name);

                try {
                    Files.createSymbolicLink(link, target);
                    Pandomium.getLogger().info("Creating symlink " + link + " to " + target);
                } catch (AccessDeniedException e) {
                    Pandomium.getLogger().error("Pandomium requires permission to " + bin.toString() + " directory");
                }
            }
        }

        loader.updateProgress(100);
        loader.callListeners(PandomiumProgressListener.State.DONE);
    }

}
