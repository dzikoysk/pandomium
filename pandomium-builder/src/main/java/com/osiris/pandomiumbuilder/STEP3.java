package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import org.kohsuke.github.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.osiris.pandomiumbuilder.Constants.*;

public class STEP3 {
    public GHRepository repo;

    public STEP3(String fullTagName, List<File> fatJars) throws NotLoadedException, XPathExpressionException, IOException, ParserConfigurationException, SAXException, TransformerException {

        System.out.println(" ");
        System.out.println("STEP 3: Make repo ready for release and maven publishing.");
        System.out.println(" ");

        File pandomiumPomFile = new File(DIR + "/pandomium/pom.xml");
        File pandomiumParentPomFile = new File(DIR + "/pom.xml");
        Document pandomiumPom = U.toXML(pandomiumPomFile);
        Document pandomiumParentPom = U.toXML(pandomiumParentPomFile);

        // Update the projects versions in its pom.xml files
        U.findNodes(pandomiumPom, "//version").item(0)
                .setTextContent(fullTagName);
        U.findNodes(pandomiumParentPom, "//version").item(0)
                .setTextContent(fullTagName);

        // Remove old <dependency> nodes:
        String groupIdNAME = "org.panda-lang.pandomium-fat-jars";
        for (Node oldGroupId :
                U.findNodesWithText(U.findNodes(pandomiumPom, "/project/dependencies/groupId"), groupIdNAME)) {
            pandomiumPom.removeChild(oldGroupId.getParentNode());
        }
        for (Node oldGroupId :
                U.findNodesWithText(U.findNodes(pandomiumParentPom, "/project/dependencies/groupId"), groupIdNAME)) {
            pandomiumParentPom.removeChild(oldGroupId.getParentNode());
        }

        // Append new:
        for (File fatJar :
                fatJars) {
            try {
                Node dependencies = U.findNodes(pandomiumPom, "/project/dependencies").item(0);
                Node dependency = null;
                try {
                    dependency = U.findNodes(pandomiumPom, "/project/dependencies/dependency/groupId[text()='" + groupIdNAME + "']").item(0);
                } catch (Exception ignored) {
                }

                if (dependency != null)
                    dependencies.removeChild(dependency.getParentNode());

                dependency = pandomiumPom.createElement("dependency");
                dependencies.appendChild(dependency);

                Node groupId = pandomiumPom.createElement("groupId");
                groupId.setTextContent(groupIdNAME);
                Node artifactId = pandomiumPom.createElement("artifactId");
                artifactId.setTextContent(U.getFileNameWithoutExt(fatJar));
                Node version = pandomiumPom.createElement("version");
                version.setTextContent("${project.version}");
                dependency.appendChild(pandomiumPom.createComment("Auto-generated by Pandomium-Builder.jar"));
                dependency.appendChild(groupId);
                dependency.appendChild(artifactId);
                dependency.appendChild(version);
            } catch (Exception exception) {
                throw exception;
            }

            // Do almost the same stuff for the pandonium parent pom.xml
            try {
                Node dependencies = U.findNodes(pandomiumParentPom, "/project/dependencyManagement/dependencies").item(0);
                Node dependency = null;
                try {
                    dependency = U.findNodes(pandomiumParentPom, "/project/dependencyManagement/dependencies/dependency/groupId[text()='" + groupIdNAME + "']").item(0);
                } catch (Exception ignored) {
                }

                if (dependency != null)
                    dependencies.removeChild(dependency.getParentNode());


                dependency = pandomiumParentPom.createElement("dependency");
                dependencies.appendChild(dependency);

                Node groupId = pandomiumParentPom.createElement("groupId");
                groupId.setTextContent(groupIdNAME);
                Node artifactId = pandomiumParentPom.createElement("artifactId");
                artifactId.setTextContent(U.getFileNameWithoutExt(fatJar));
                dependency.appendChild(pandomiumParentPom.createComment("Auto-generated by Pandomium-Builder.jar"));
                dependency.appendChild(groupId);
                dependency.appendChild(artifactId);
            } catch (Exception exception) {
                throw exception;
            }
        }

        // Update files
        U.saveXMLToFile(pandomiumPom, pandomiumPomFile);
        U.saveXMLToFile(pandomiumParentPom, pandomiumParentPomFile);

        // Create maven-publish.sh script
        File mavenInstallScript = new File(DIR + "/maven-publish.sh");
        if (!mavenInstallScript.exists()) mavenInstallScript.createNewFile();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mavenInstallScript))) {
            bw.write("# Auto-generated by Pandomium-Builder.jar\n");
            for (File fatJar :
                    fatJars) {
                bw.write("\n");
                bw.write("mvn --batch-mode deploy:deploy-file -Durl="+ MAVEN_REPO_URL +"\\\n");
                bw.write("                       -DrepositoryId="+MAVEN_REPO_ID+" \\\n");
                bw.write("                       -Dfile="+fatJar.getName()+" \\\n");
                bw.write("                       -DgroupId="+groupIdNAME+" \\\n");
                bw.write("                       -DartifactId="+U.getFileNameWithoutExt(fatJar)+" \\\n");
                bw.write("                       -Dversion="+fullTagName+" \\\n");
                bw.write("                       -Dpackaging=jar \\\n");
                bw.write("                       -DgeneratePom=true \\\n");
                bw.write("                       -DgeneratePom.description=\"Contains OS-specific JCEF code.\" \\\n");
            }
            bw.write("\n");
            bw.write("mvn --batch-mode deploy\n"); // To deploy pandonium, after the os-specific fat jars have been deployed
            bw.flush();
        }
        System.out.println("Authenticating with token...");
        GitHub github = new GitHubBuilder().withOAuthToken(O_AUTH_TOKEN).build();
        System.out.println("Success!");
        System.out.println("Getting repo: '" + OWNER_AND_REPO + "'...");
        repo = github.getRepository(OWNER_AND_REPO);
        System.out.println("Success!");
        System.out.println("Committing updated files...");
        GHRef mainRef = repo.getRef("heads/master");
        String mainTreeSha = repo.getTreeRecursive("master", 1).getSha();
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(mainTreeSha);
        commitFile(treeBuilder, mavenInstallScript);
        commitFile(treeBuilder, pandomiumParentPomFile);
        commitFile(treeBuilder, pandomiumPomFile);
        String treeSha = treeBuilder.create().getSha();
        GHCommit commit = repo.createCommit().message("Add files")
                .tree(treeSha)
                .message(fullTagName)
                .author("Pandomium-Builder", "pandomium@builder.com", new Date())
                .committer("Pandomium-Builder", "pandomium@builder.com", new Date())
                .parent(mainRef.getObject().getSha())
                .create();
        String commitSha = commit.getSHA1();
        mainRef.updateTo(commitSha);
        System.out.println("Success!");
    }

    private void commitFile(GHTreeBuilder treeBuilder, File file) throws IOException {
        String fileInTree = file.getAbsolutePath().replace(DIR.getAbsolutePath(), "");
        if (fileInTree.startsWith("/"))
            fileInTree = fileInTree.substring(1);
        else if (fileInTree.startsWith("\\")) {// Is equal to one \
            fileInTree = fileInTree.substring(1);
            if (fileInTree.startsWith("\\")) // Maybe there are two \\ slashes at the beginning
                fileInTree = fileInTree.substring(1);
        }
        System.out.println("Commiting '" + fileInTree + "'. Full path: '" + file.getAbsolutePath() + "'." + fileInTree);
        treeBuilder.add(fileInTree, Files.readAllBytes(file.toPath()), false);
    }

}
