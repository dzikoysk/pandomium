package org.panda_lang.pandomium.loader;

import net.dzikoysk.linuxenv.LinuxJVMEnvironment;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.FileUtils;
import org.panda_lang.pandomium.util.SystemUtils;
import org.panda_lang.pandomium.util.os.PandomiumOS;

import java.io.File;

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

                Pandomium.getLogger().warn("You have to create symlink: ln -s " + nativePath + File.separator + name + " " + bin.getAbsolutePath() + File.separator + name);
            }
        }

        loader.updateProgress(100);
        loader.callListeners(PandomiumProgressListener.State.DONE);
    }

}
