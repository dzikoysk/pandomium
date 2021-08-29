package org.panda_lang.pandomium.loader;

import com.google.gson.JsonElement;
import com.osiris.autoplug.core.json.JsonTools;
import com.osiris.dyml.DreamYaml;
import net.dzikoysk.dynamiclogger.Logger;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.loader.PandomiumProgressListener.State;
import org.panda_lang.pandomium.loader.os.PandomiumLinuxNativesLoader;
import org.panda_lang.pandomium.settings.categories.LoaderSettings;
import org.panda_lang.pandomium.util.SystemUtils;
import org.panda_lang.pandomium.util.UtilsOS;
import org.panda_lang.pandomium.util.UtilsVersion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PandomiumLoader {

    private final Pandomium pandomium;
    private final Logger log;
    private final Collection<PandomiumProgressListener> progressListeners;
    private int progress;

    public PandomiumLoader(Pandomium pandomium) {
        this.pandomium = pandomium;
        this.log = pandomium.getLogger();
        this.progressListeners = new ArrayList<>();
    }

    public void load() {

        log.info("Preparing Pandonium, fetching version details...");
        String ownerAndRepo = "Osiris-Team/pandomium"; // TODO dzikoysk/pandomium
        String latestVersion = null;
        String downloadURL = null;
        try {
            List<String> downloadURLS = new ArrayList<>();
            latestVersion = new JsonTools()
                    .getJsonObject("https://api.github.com/repos/" + ownerAndRepo + "/releases/latest")
                    .get("tag_name").getAsString();
            for (JsonElement element :
                    new JsonTools()
                            .getJsonObject("https://api.github.com/repos/" + ownerAndRepo + "/releases/latest")
                            .getAsJsonArray("assets")) {
                String url = element.getAsJsonObject().get("browser_download_url").getAsString();
                if (url.endsWith(".zip"))
                    downloadURLS.add(url);
            }


            UtilsOS.OperatingSystemArchitectureType osArchitectureType = null;
            String actualOsArchitecture = System.getProperty("os.arch").toLowerCase();
            for (UtilsOS.OperatingSystemArchitectureType type :
                    UtilsOS.OperatingSystemArchitectureType.values()) {
                if (actualOsArchitecture.equals(type.toString().toLowerCase())) // Not comparing the actual names because the enum has more stuff matching one name
                    osArchitectureType = type;
            }
            if (osArchitectureType == null) {
                // Do another check.
                // On windows it can be harder to detect the right architecture that's why we do the stuff below:
                // Source: https://stackoverflow.com/questions/4748673/how-can-i-check-the-bitness-of-my-os-using-java-j2se-not-os-arch/5940770#5940770
                String arch = System.getenv("PROCESSOR_ARCHITECTURE");
                String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
                boolean is64 = arch != null && arch.endsWith("64")
                        || wow64Arch != null && wow64Arch.endsWith("64"); // Otherwise its 32bit
                if (is64)
                    osArchitectureType = UtilsOS.OperatingSystemArchitectureType.X64;
                else
                    osArchitectureType = UtilsOS.OperatingSystemArchitectureType.X32;
                log.debug("The current operating systems architecture '" + actualOsArchitecture +
                        "' was not found in the architectures list '" + Arrays.toString(UtilsOS.OperatingSystemArchitectureType.values()) + "'." +
                        " Defaulting to '" + osArchitectureType + "'.");
            }
            log.info("Determined '" + osArchitectureType.name() + "' as operating systems architecture.");

            // Set the operating systems type
            UtilsOS.OperatingSystemType osType;
            String actualOsType = System.getProperty("os.name").toLowerCase();
            if (actualOsType.contains("alpine"))
                osType = UtilsOS.OperatingSystemType.ALPINE_LINUX;
            if (actualOsType.contains("win"))
                osType = UtilsOS.OperatingSystemType.WINDOWS;
            else if (actualOsType.contains("mac"))
                osType = UtilsOS.OperatingSystemType.MAC;
            else if (actualOsType.contains("aix"))
                osType = UtilsOS.OperatingSystemType.AIX;
            else if (actualOsType.contains("nix")
                    || actualOsType.contains("nux"))
                osType = UtilsOS.OperatingSystemType.LINUX;
            else if (actualOsType.contains("sunos"))
                osType = UtilsOS.OperatingSystemType.SOLARIS;
            else {
                osType = UtilsOS.OperatingSystemType.LINUX;
                log.warn("The current operating system '" + actualOsType + "' was not found in the supported operating systems list." +
                        " Defaulting to '" + UtilsOS.OperatingSystemType.LINUX.name() + "'.");
            }
            log.info("Determined '" + osType.name() + "' as operating system.");

            // Determine the right download url for this system
            for (String url :
                    downloadURLS) {
                String fileName = new File(url).getName();
                for (String os :
                        osType.getAliases()) {
                    if (fileName.contains(os))
                        for (String arch :
                                osArchitectureType.getAliases()) {
                            if (fileName.contains(arch)) {
                                downloadURL = url;
                                break;
                            }
                        }
                }
            }

            if (downloadURL == null)
                log.warn("Failed to find update file for this system.");
        } catch (Exception e) {
            log.error("Failed to fetch latest version details. Message: " + e.getMessage());
            e.printStackTrace();
        }

        // Compare currently installed version with latest
        boolean installUpdate = false;
        try {
            DreamYaml yml = new DreamYaml(pandomium.getSettings().getNatives().getNativeDirectory() + "/pandonium.yml");
            if (yml.getFile().exists()) {
                yml.load();
                String currentVersion = yml.get("version").asString();
                if (new UtilsVersion().compare(currentVersion, latestVersion)) {
                    log.info("Update found: '" + currentVersion + "' -> '" + latestVersion + "'");
                    installUpdate = true;
                } else
                    log.info("Pandonium is on the latest version.");
            } else {
                log.info("No 'natives' installation found.");
                installUpdate = true;
            }
        } catch (Exception e) {
            log.error("Failed to compare versions. Message: " + e.getMessage());
            e.printStackTrace();
        }

        log.info("#");
        log.info("# Launching " + latestVersion);
        log.info("#");

        LoaderSettings loaderSettings = pandomium.getSettings().getLoader();

        progressListeners.add((state, progress) -> {
            if (state == State.RUNNING) {
                log.info("Progress: " + progress + "%");
            }
        });

        boolean finalInstallUpdate = installUpdate;
        String finalDownloadURL = downloadURL;
        Runnable runnable = () -> {
            try {
                this.updateProgress(0);

                PandomiumNativesLoader nativeLoader = new PandomiumNativesLoader();
                nativeLoader.loadNatives(this, finalInstallUpdate, finalDownloadURL);

                Pandomium pandomium = this.getPandomium();
                String nativePath = pandomium.getSettings().getNatives().getNativeDirectory().getAbsolutePath();
                SystemUtils.injectLibraryPath(nativePath);

                if (UtilsOS.isLinux()) {
                    PandomiumLinuxNativesLoader linuxNativesLoader = new PandomiumLinuxNativesLoader();
                    linuxNativesLoader.loadLinuxNatives(pandomium, nativePath);
                }

                this.updateProgress(100);
                this.callListeners(PandomiumProgressListener.State.DONE);
            } catch (Exception e) {
                log.error("Failed to install JCEF update. Message: " + e.getMessage());
                e.printStackTrace();
            }
        };

        if (loaderSettings.isLoadAsync())
            new Thread(runnable, "Pandomium Loader Thread").start();
        else
            runnable.run();

    }

    protected void updateProgress(int newProgress) {
        this.progress = newProgress;
        callListeners(PandomiumProgressListener.State.RUNNING);
    }

    protected void callListeners(PandomiumProgressListener.State state) {
        for (PandomiumProgressListener listener : progressListeners) {
            listener.onUpdate(state, progress);
        }
    }

    public void addProgressListener(PandomiumProgressListener listener) {
        progressListeners.add(listener);
    }

    public Collection<PandomiumProgressListener> getProgressListeners() {
        return progressListeners;
    }

    public int getProgress() {
        return progress;
    }

    public Pandomium getPandomium() {
        return pandomium;
    }

}
