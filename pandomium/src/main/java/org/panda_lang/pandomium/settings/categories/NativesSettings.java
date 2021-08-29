package org.panda_lang.pandomium.settings.categories;

import java.io.File;

public class NativesSettings {

    private File nativeDirectory;

    public File getNativeDirectory() {
        return nativeDirectory;
    }

    public void setNativeDirectory(String nativeDirectory) {
        setNativeDirectory(new File(nativeDirectory));
    }

    public void setNativeDirectory(File nativeDirectory) {
        this.nativeDirectory = nativeDirectory;
    }

}
