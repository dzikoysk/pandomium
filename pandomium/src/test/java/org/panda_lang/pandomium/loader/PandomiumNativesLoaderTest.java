package org.panda_lang.pandomium.loader;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;
import org.panda_lang.pandomium.util.JarUtils;
import org.panda_lang.pandomium.util.JsonUtils;
import org.panda_lang.pandomium.util.OSUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PandomiumNativesLoaderTest {

    @Test
    void testDownloadUrlFetching() throws Exception {
        String ownerAndRepo = "dzikoysk/pandomium";
        String version = "1.0.0-ALPHA7-JCEF-v1.0.10-92.0.25+gd15cfa8+chromium-92.0.4515.131";
        String latestReleaseGithubUrl = "https://api.github.com/repos/" + ownerAndRepo + "/releases/tags/" + version;
        System.out.println(latestReleaseGithubUrl);
        List<String> downloadURLS = new ArrayList<>();
        for (JsonElement element :
                new JsonUtils()
                        .getJsonObject(latestReleaseGithubUrl)
                        .getAsJsonArray("assets")) {
            String url = element.getAsJsonObject().get("browser_download_url").getAsString();
            if (url.endsWith(".tar.xz"))
                downloadURLS.add(url);
        }

        // Determine the right download url for this system
        String downloadURL = null;
        for (String url :
                downloadURLS) {
            System.out.println("Checking "+url);
            String fileName = new File(url).getName();
            for (String osAlias :
                    OSUtils.OS_TYPE.getAliases()) {
                if (fileName.contains(osAlias))
                    for (String archAlias :
                            OSUtils.OS_ARCH.getAliases()) {
                        if (fileName.contains(archAlias)) {
                            downloadURL = url;
                            break;
                        }
                    }
            }
        }

        if (downloadURL == null)
            throw new Exception("Failed to find JCEF natives download url for this system.");
        System.out.println("Found: "+downloadURL);
    }
}