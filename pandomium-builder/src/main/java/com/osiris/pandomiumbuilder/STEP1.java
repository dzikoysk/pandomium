package com.osiris.pandomiumbuilder;

import com.google.gson.JsonElement;
import com.osiris.autoplug.core.json.JsonTools;
import com.osiris.betterthread.BetterThread;
import com.osiris.betterthread.BetterThreadManager;
import com.osiris.betterthread.BetterWarning;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.osiris.pandomiumbuilder.Constants.*;

public class STEP1 {
    public String tagNameJCEF;
    public String fullTagName;
    public List<File> downloadedJCEFBuilds;

    public STEP1(String[] args) throws Exception {
        System.out.println(" ");
        System.out.println("STEP 1: Download the latest JCEF builds.");
        System.out.println(" ");

        for (String arg :
                args) {
            // Example "dir:C://User/John" get splits into command="dir" and value="C://User/John"
            String command = arg.substring(0, arg.indexOf(":"));
            String value = arg.substring(arg.indexOf(":") + 1);
            if (command.equalsIgnoreCase("dir"))
                DIR = new File(value);
            else if (command.equalsIgnoreCase("abort_on_warning"))
                isAbortOnWarning = Boolean.parseBoolean(value);
            else if (command.equalsIgnoreCase("o_auth_token"))
                O_AUTH_TOKEN = value;
            else if (command.equalsIgnoreCase("owner_and_repo"))
                OWNER_AND_REPO = value;
            else if (command.equalsIgnoreCase("version"))
                VERSION = value;
            else if (command.equalsIgnoreCase("release_notes_url"))
                RELEASE_NOTES_URL = value;
            else if (command.equalsIgnoreCase("path_to_maven_repo"))
                PATH_TO_MAVEN_REPO = value;
            else if (command.equalsIgnoreCase("maven_repo_id"))
                MAVEN_REPO_ID = value;
        }

        if (DIR == null)
            DIR = new File(System.getProperty("user.dir"));

        if (!DIR.exists())
            DIR.mkdirs();

        if (O_AUTH_TOKEN == null)
            throw new Exception("Argument 'o_auth_token' is missing! Add it in this format:" +
                    " 'o_auth_token:TOKEN_HERE' to '... -jar Pandomium-Builder.jar <argument1> <argument2>...'.");

        if (OWNER_AND_REPO == null)
            throw new Exception("Argument 'owner_and_repo' is missing! Add it in this format:" +
                    " 'owner_and_repo:Owner/Repo' to '... -jar Pandomium-Builder.jar <argument1> <argument2>...'.");

        if (VERSION == null)
            throw new Exception("Argument 'path_to_maven_repo' is missing! Add it in this format:" +
                    " 'version:1.0.0' to '... -jar Pandomium-Builder.jar <argument1> <argument2>...'.");

        if (PATH_TO_MAVEN_REPO == null)
            throw new Exception("Argument 'version' is missing! Add it in this format:" +
                    " 'path_to_maven_repo:host.com/path/to/repo' to '... -jar Pandomium-Builder.jar <argument1> <argument2>...'.");

        if (MAVEN_REPO_ID == null)
            throw new Exception("Argument 'maven_repo_id' is missing! Add it in this format:" +
                    " 'maven_repo_id:REPO_ID_HERE' to '... -jar Pandomium-Builder.jar <argument1> <argument2>...'.");

        tagNameJCEF = new JsonTools()
                .getJsonObject("https://api.github.com/repos/jcefbuild/jcefbuild/releases/latest")
                .get("tag_name").getAsString();
        fullTagName = VERSION + "-JCEF-" + tagNameJCEF;
        List<String> downloadURLS = new ArrayList<>();
        for (JsonElement element :
                new JsonTools()
                        .getJsonObject("https://api.github.com/repos/jcefbuild/jcefbuild/releases/latest")
                        .getAsJsonArray("assets")) {
            String downloadURL = element.getAsJsonObject().get("browser_download_url").getAsString();
            if (downloadURL.endsWith(".zip"))
                downloadURLS.add(downloadURL);
        }

        if (downloadURLS.isEmpty())
            throw new Exception("Aborted build! No JCEF builds found as assets in: https://api.github.com/repos/jcefbuild/jcefbuild/releases/latest");

        boolean isLinuxBuildAvailable = false;
        boolean isWindowsBuildAvailable = false;
        for (String downloadURL :
                downloadURLS) {
            if (downloadURL.contains("win"))
                isWindowsBuildAvailable = true;
            if (downloadURL.contains("linux"))
                isLinuxBuildAvailable = true;
        }

        if (!isLinuxBuildAvailable) {
            System.out.println("WARNING: No JCEF Linux build was found to download!");
            if (isAbortOnWarning)
                System.exit(1);
        }

        if (!isWindowsBuildAvailable) {
            System.out.println("WARNING: No JCEF Windows build was found to download!");
            if (isAbortOnWarning)
                System.exit(1);
        }

        BetterThreadManager manager = new BetterThreadManager();
        for (String downloadURL :
                downloadURLS) {
            new DownloadTask("Download", manager, downloadURL, DIR, "zip")
                    .start();
        }

        while (!manager.isFinished()) {
            Thread.sleep(1000);
            System.out.println();
            for (BetterThread downloadTask :
                    manager.getAll()) {
                System.out.println(downloadTask.getStatus());
            }
        }

        for (BetterThread downloadTask :
                manager.getAll()) {
            if (downloadTask.getWarnings().size() > 0) {
                String warnings = " |STACKTRACE-START|";
                for (BetterWarning warning :
                        downloadTask.getWarnings()) {
                    warnings = warnings + warning.getExtraInfo();
                    if (warning.getException() != null)
                        warnings = warnings + " Exception: " + warning.getException().getMessage() + " Stacktrace: " + Arrays.toString(warning.getException().getStackTrace());
                    warnings = warnings + " |STACKTRACE-END|";
                }
                throw new Exception("Error during download. Details: " + warnings);
            }
        }

        downloadedJCEFBuilds = new ArrayList<>();
        System.out.println();
        for (int i = 0; i < manager.getAll().size(); i++) {
            DownloadTask downloadTask = ((DownloadTask) manager.getAll().get(i));
            downloadedJCEFBuilds.add(downloadTask.getDest());
            System.out.println(downloadTask.getDest().getAbsolutePath());
        }
        System.out.println("Downloaded all files successfully!");
    }
}
