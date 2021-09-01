package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHReleaseBuilder;
import org.kohsuke.github.GHRepository;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.osiris.pandomiumbuilder.Constants.RELEASE_NOTES_URL;
import static com.osiris.pandomiumbuilder.Constants.VERSION;

public class STEP4 {

    public STEP4(String fullTagName, String tagNameJCEF, GHRepository repo, List<File> filesToUpload) throws NotLoadedException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        System.out.println(" ");
        System.out.println("STEP 4: Create release and upload assets.");
        System.out.println(" ");

        System.out.println("Creating release with tag '" + tagNameJCEF + "'...");
        GHRelease release = new GHReleaseBuilder(repo, fullTagName)
                .prerelease(false)
                .name(fullTagName)
                .body("#### Maven\n" +
                        "```xml\n" +
                        "<dependency>\n" +
                        "    <groupId>org.panda-lang</groupId>\n" +
                        "    <artifactId>pandomium</artifactId>\n" +
                        "    <version>"+fullTagName+"</version>\n" +
                        "</dependency>\n" +
                        "```\n" +
                        "```xml\n" +
                        "<repository>\n" +
                        "    <id>panda-repository</id>\n" +
                        "    <url>https://repo.panda-lang.org/</url>\n" +
                        "</repository>\n" +
                        "```\n" +
                        "Release notes: " + RELEASE_NOTES_URL)
                .create();
        System.out.println("Success!");
        System.out.println("Uploading assets to release...");
        for (File f :
                filesToUpload) {
            System.out.println("Uploading: " + f);
            release.uploadAsset(f, Files.probeContentType(f.toPath()));
        }
        System.out.println("Uploaded all files successfully!");
    }
}
