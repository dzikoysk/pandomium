package org.panda_lang.pandomium;

import net.dzikoysk.dynamiclogger.Journalist;
import net.dzikoysk.dynamiclogger.Logger;
import org.cef.CefApp;
import org.cef.CefSettings;
import org.panda_lang.pandomium.loader.PandomiumLoader;
import org.panda_lang.pandomium.loader.PandomiumProgressListener;
import org.panda_lang.pandomium.settings.categories.CommandLineSettings;
import org.panda_lang.pandomium.settings.categories.NativesSettings;
import org.panda_lang.pandomium.util.JarUtils;
import org.panda_lang.pandomium.util.OSUtils;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import java.awt.*;
import java.io.File;

public class Pandomium implements Journalist {
    public static String FULL_VERSION = null;

    static {
        try {
            FULL_VERSION = new JarUtils().getThisJarsPandoniumProperties().getProperty("version");
        } catch (Exception exception) {
            try {
                // If this throws an exception it usually means we are in a test environment
                // Search for the pom.xml file:
                File dir = new File(System.getProperty("user.dir"));
                JarUtils jarUtils = new JarUtils();
                File pom = jarUtils.searchFileRecursively("pom.xml", dir);
                FULL_VERSION = jarUtils.findVersion(pom);
            } catch (Exception e) {
                System.err.println("Failed to find version after multiple tries. Cannot run Pandomium without version information!");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates a new {@link PandomiumBuilder} object with default values and returns it.
     */
    public static PandomiumBuilder builder() {
        return new PandomiumBuilder()
                .nativeDirectory("natives");
    }

    /**
     * Creates a new {@link Pandomium} object with default values and returns it.
     */
    public static Pandomium buildDefault() {
        return builder().build();
    }

    private final Logger logger;
    private final CommandLineSettings commandLine;
    private final NativesSettings natives;
    private final CefSettings cefSettings;
    private final PandomiumLoader loader;
    private final Thread mainThread;

    private static CefApp CEF_APP = null;


    public Pandomium(Logger logger, CommandLineSettings commandLine, NativesSettings natives) {
        this.logger = logger;
        this.commandLine = commandLine;
        this.natives = natives;

        this.cefSettings = new CefSettings();
        this.cefSettings.windowless_rendering_enabled = OSUtils.isLinux();
        this.loader = new PandomiumLoader(this);
        this.mainThread = Thread.currentThread();

        if (CEF_APP==null || (CefApp.getState()!=null && CefApp.getState().equals(CefApp.CefAppState.TERMINATED))){ // To ensure, init is only called once per runtime
            // Init:
            Toolkit.getDefaultToolkit();

            loader.addProgressListener((state, progress) -> {
                if (state != PandomiumProgressListener.State.DONE) {
                    return;
                }

                CEF_APP = CefApp.getInstance(this.getCommandLine().getArguments(), this.getCefSettings());
            });

            loader.load();
        }
    }

    /**
     * Creates a visible {@link PandomiumClient}.
     */
    public PandomiumClient createClient() {
        return createClient(false, false);
    }

    /**
     * Creates an invisible/background {@link PandomiumClient}.
     */
    public PandomiumClient createOffscreenClient() {
        return createClient(true, true);
    }

    public PandomiumClient createClient(boolean isOffscreenRendered, boolean isTransparent) {
        if (CEF_APP == null) throw new RuntimeException("Pandomium is not initialized yet!");
        return new PandomiumClient(this.CEF_APP, isOffscreenRendered, isTransparent);
    }

    public CefApp getCefApp() {
        return CEF_APP;
    }

    public PandomiumLoader getLoader() {
        return loader;
    }

    public NativesSettings getNatives() {
        return natives;
    }

    public CommandLineSettings getCommandLine() {
        return commandLine;
    }

    public CefSettings getCefSettings() {
        return cefSettings;
    }

    public Logger getLogger() {
        return logger;
    }

}
