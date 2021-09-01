package com.osiris.pandomiumbuilder;

public class Main {

    /**
     * Tested and working on JDK/JRE 14. <br>
     * Not working on JDK/JRE 8 and 16. <br>
     * Example: <br>
     * <pre>
     * java -jar Pandomium-builder.jar o_auth_token:INSERT_TOKEN_HERE owner_and_repo:INSERT_HERE dir:INSERT_DIR_PATH abort-on-warning:false <br>
     * </pre>
     * Note that the order in which the arguments are passed doesn't matter. <br><br>
     * Needed arguments: <br>
     * o_auth_token <small>| Must be given if you want to create and publish a release on GitHub.</small> <br>
     * owner_and_repo <small>| Format: Owner/Repository. The repository where to create the release.</small> <br>
     * path_to_maven_repo <small>| The maven repos scp-url. Format: host.com/path/to/repo</small> <br>
     * maven_repo_id <small>| Must be given, to publish the os-specific fat-jars to the maven repo.</small> <br>
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
}
