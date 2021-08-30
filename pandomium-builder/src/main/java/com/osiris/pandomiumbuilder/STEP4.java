package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import org.kohsuke.github.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static com.osiris.pandomiumbuilder.Main.*;

public class STEP4 {

    public STEP4(String fullTagName, String tagNameJCEF, GHRepository repo, List<File> filesToUpload) throws NotLoadedException, XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        System.out.println(" ");
        System.out.println("STEP 4: Create release and upload assets.");
        System.out.println(" ");

        System.out.println("Creating release with tag '"+tagNameJCEF+"'...");
        GHRelease release = new GHReleaseBuilder(repo, fullTagName)
                .prerelease(false)
                .name(fullTagName)
                .body("Release notes: "+RELEASE_NOTES_URL)
                .create();
        System.out.println("Success!");
        System.out.println("Uploading assets to release...");
        for (File f :
                filesToUpload) {
            System.out.println("Uploading: "+f);
            release.uploadAsset(f, Files.probeContentType(f.toPath()));
        }
        System.out.println("Uploaded all files successfully!");
    }
}
