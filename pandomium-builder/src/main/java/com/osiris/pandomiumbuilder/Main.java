package com.osiris.pandomiumbuilder;

import net.lingala.zip4j.ZipFile;
import org.kohsuke.github.*;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static File DIR;
    public static boolean isAbortOnWarning = false;
    public static String O_AUTH_TOKEN;
    public static String OWNER_AND_REPO;
    public static String VERSION;
    public static String RELEASE_NOTES_URL = "";

    /**
     * Tested and working on JDK/JRE 14. <br>
     * Not working on JDK/JRE 8 and 16. <br>
     * Example: <br>
     * <pre>
     * java -jar Pandomium-builder.jar o_auth_token:INSERT_TOKEN_HERE owner_and_repo:INSERT_HERE dir:INSERT_DIR_PATH abort-on-warning:false <br>
     * </pre>
     * Note that the order in which the arguments are passed doesn't matter. <br><br>
     * Needed arguments: <br>
     * o_auth_token <small>| Must be given if you want to create and publish a release.</small> <br>
     * owner_and_repo <small>| Format: Owner/Repository. The repository where to create the release.</small> <br>
     * version <small>| Pandomiums version.</small> <br><br>
     * Optional arguments: <br>
     * dir <small>| The working directory path. If not given the current working directory is used. If not existing gets created.</small> <br>
     * abort_on_warning <small>| Can be true or false (default is false). Should the program abort on a warning.</small> <br>
     * release_notes_url <small>| Url to the release notes.</small> <br>
     */
    public static void main(String[] args) throws Exception {
        STEP1 step1 = new STEP1(args);
        STEP2 step2 = new STEP2(step1.downloadedJCEFBuilds, step1.fullTagName, step1.tagNameJCEF);
        STEP3 step3 = new STEP3(step1.fullTagName, step2.fatJars);
        new STEP4(step1.fullTagName, step1.tagNameJCEF, step3.repo, step2.filesToUpload);
    }

    public static boolean deleteDirectoryRecursively(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectoryRecursively(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

}
