package org.panda_lang.pandomium.loader.os;

import net.dzikoysk.linuxenv.LinuxJVMEnvironment;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.util.FileUtils;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PandomiumLinuxNativesLoader {

    public void loadLinuxNatives(String nativePath) throws Exception {
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

        String[] libFiles = new String[] { "libgluegen-rt.so", "libjogl_desktop.so", "libnativewindow_awt.so", "libnativewindow_x11.so", "libnewt.so" };
        File nativesDirectory = new File(nativePath);
        File[] nativeFiles = nativesDirectory.listFiles();

        for (String name : libFiles) {
            if (FileUtils.isIn(name, nativeFiles)) {
                continue;
            }

            try {
                Files.copy(Pandomium.class.getResourceAsStream("/" + name), new File(nativesDirectory, name).toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
