package com.osiris.pandomiumbuilder;

import com.osiris.dyml.exceptions.NotLoadedException;
import com.osiris.pandomiumbuilder.tar.TarXz;
import net.lingala.zip4j.ZipFile;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static com.osiris.pandomiumbuilder.Constants.DIR;
import static com.osiris.pandomiumbuilder.Constants.VERSION;
import static com.osiris.pandomiumbuilder.U.deleteDirectoryRecursively;

public class STEP2 {
    public List<File> fatJars;
    public List<File> filesToUpload;

    public STEP2(List<File> downloadedJCEFBuilds, String fullTagName, String tagNameJCEF) throws IOException, NotLoadedException {
        System.out.println(" ");
        System.out.println("STEP 2: Create the natives zips and the fat jars.");
        System.out.println(" ");


        System.out.println("Creating natives and fat jars (this make take a bit)...");
        fatJars = new ArrayList<>();
        filesToUpload = new ArrayList<>();
        for (File downloadedJCEFBuild :
                downloadedJCEFBuilds) {

            // Do natives stuff:
            File tempDir = new File(DIR + "/temp-dir");
            if (!tempDir.exists()) tempDir.mkdirs();
            System.out.println("Created temp-dir: " + tempDir);
            System.out.println("Extracting " + downloadedJCEFBuild.getName() + " to temp-dir...");
            ArchiverFactory.createArchiver(downloadedJCEFBuild)
                    .extract(downloadedJCEFBuild, tempDir);
            System.out.println("Extracted " + downloadedJCEFBuild.getName() + " to temp-dir.");
            File source = new File(tempDir + "/java-cef-build-bin/bin/lib").listFiles()[0];

            File nativeProperties = new File(source + "/pandomium-natives.properties");
            if (nativeProperties.exists()) nativeProperties.delete();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(nativeProperties))) {
                bw.write("full-version=" + fullTagName + "\n");
                bw.write("pandomium-version=" + VERSION + "\n");
                bw.write("jcef-version=" + tagNameJCEF + "\n");
                bw.flush();
            }

            System.out.println("Creating natives...");
            File nativesTar = new TarXz("jcef-" + U.getFileNameWithoutExt(downloadedJCEFBuild) + "-natives")
                    .addFile(source).createInDir(DIR);
            System.out.println("Created natives: " + nativesTar.getAbsolutePath());
            filesToUpload.add(nativesTar);

            // Do fat jar stuff:
            class FilesFinder implements FileVisitor<Path> {
                private final Path startingDir;
                private final List<File> results = new ArrayList<>();

                public FilesFinder(Path startingDir) throws IOException {
                    this.startingDir = startingDir;
                    Files.walkFileTree(startingDir, this);
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (startingDir.equals(dir))
                        return FileVisitResult.CONTINUE;
                    else
                        return FileVisitResult.SKIP_SUBTREE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!file.toFile().getName().contains("tests"))
                        results.add(file.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                public Path getStartingDir() {
                    return startingDir;
                }

                public List<File> getResults() {
                    return results;
                }
            }

            File tempFatJar = new File(tempDir + "/fat-jar");
            if (!tempFatJar.exists()) tempFatJar.mkdirs();

            File startingDir = new File(tempDir + "/java-cef-build-bin/bin"); // Extract and combine the jar files in this folder to the tempFatJar folder
            for (File f :
                    new FilesFinder(startingDir.toPath()).getResults()) {
                ArchiverFactory.createArchiver(ArchiveFormat.JAR)
                        .extract(f, tempFatJar);
            }

            System.out.println("Creating fat jar...");
            ArchiverFactory.createArchiver(downloadedJCEFBuild)
                    .extract(downloadedJCEFBuild, tempDir);
            String archiveName2 = "jcef-" + U.getFileNameWithoutExt(downloadedJCEFBuild) + "-fat";
            File fatJar = ArchiverFactory.createArchiver(ArchiveFormat.JAR)
                    .create(archiveName2, DIR, tempFatJar); // archive name, destination and source
            System.out.println("Created fat jar: " + fatJar.getAbsolutePath());
            filesToUpload.add(fatJar);
            fatJars.add(fatJar);
            System.out.println("Deleting temp-dir...");
            deleteDirectoryRecursively(tempDir);
            System.out.println("Deleted temp-dir.");
        }
        System.out.println("Created natives and fat jars successfully!");

    }
}
