package com.osiris.pandomiumbuilder.tar;

import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.*;
import java.util.*;

/**
 * @author Osiris-Team
 */
public class TarXz implements ArchiveFile{
    private String fileName;
    private List<File> filesToAdd = new ArrayList<>();

    /**
     * Note that to create a tar.xz file you will first have to add files <br>
     * with {@link #addFile(File...)} and then generate the compressed .tar.xz file with {@link #createInDir(File)}.
     * @param fileName without file extension.
     */
    public TarXz(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Can be file or folder.
     */
    public TarXz addFile(File... files){
        Objects.requireNonNull(files);
        filesToAdd.addAll(Arrays.asList(files));
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public List<File> getFilesToAdd() {
        return filesToAdd;
    }


    /**
     * Creates a new, compressed .tar.xz file, containing all the added files, in the given outputDir. <br>
     * If the file already exists it gets overwritten.
     */
    @Override
    public File createInDir(File dir) throws IOException {
        // First create an uncompressed tar
        Tar tar = new Tar(fileName);
        for (File f :
                filesToAdd) {
            tar.addFile(f);
        }
        File tarFile = tar.createInDir(dir);

        // Finally create the .tar.xz file
        File generatedFile = new File(dir+"/"+fileName+".tar.xz");
        if (generatedFile.exists()) generatedFile.delete();
        generatedFile.createNewFile();

        FileOutputStream fileOs = new FileOutputStream(generatedFile);
        LZMA2Options options = new LZMA2Options();
        options.setPreset(7); // play with this number: 6 is default but 7 works better for mid sized archives ( > 8mb)
        XZOutputStream xzOut = new XZOutputStream(fileOs, options);
        byte[] buf = new byte[8192];
        int size;
        FileInputStream in = new FileInputStream(tarFile);
        while ((size = in.read(buf)) != -1)
            xzOut.write(buf, 0, size);
        xzOut.finish();
        tarFile.delete();
        return generatedFile;
    }

    @Override
    public File unpackInDir(File dir) {
        //TODO
        return null;
    }
}
