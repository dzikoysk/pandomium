package com.osiris.pandomiumbuilder.tar;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

/**
 * @author Osiris-Team
 */
public class TarGz implements ArchiveFile{
    private String fileName;
    private List<File> filesToAdd = new ArrayList<>();

    /**
     * Note that to create a tar.gz file you will first have to add files <br>
     * with {@link #addFile(File...)} and then generate the compressed .tar.gz file with {@link #createInDir(File)}.
     * @param fileName without file extension.
     */
    public TarGz(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Can be file or folder.
     */
    public TarGz addFile(File... files){
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
     * Creates a new, compressed .tar.gz file, containing all the added files, in the given outputDir. <br>
     * If the file already exists it gets overwritten.
     */
    @Override
    public File createInDir(File dir) throws IOException {
        File tarFile = new File(dir+"/"+fileName+".tar.gz");
        TarArchiveOutputStream tarOs = null;
        try {
            GZIPOutputStream gos = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(tarFile)));
            tarOs = new TarArchiveOutputStream(gos);
            for (File f :
                    filesToAdd) {
                writeFileToTar(f.getAbsolutePath(), "", tarOs);
            }
        } catch (IOException e) {
            if(tarOs!=null) tarOs.close();
            throw e;
        }
        return tarFile;
    }

    @Override
    public File unpackInDir(File dir) {
        // TODO
        return null;
    }


    private void writeFileToTar(String filePath, String parentPath, TarArchiveOutputStream tarArchive) throws IOException {
        File file = new File(filePath);
        // Create entry name relative to parent file path
        String entryName = parentPath + file.getName();
        // add tar ArchiveEntry
        tarArchive.putArchiveEntry(new TarArchiveEntry(file, entryName));
        if(file.isFile()){
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            // Write file content to archive
            IOUtils.copy(bis, tarArchive);
            tarArchive.closeArchiveEntry();
            bis.close();
        }else if(file.isDirectory()){
            // no need to copy any content since it is
            // a directory, just close the outputstream
            tarArchive.closeArchiveEntry();
            // for files in the directories
            for(File f : file.listFiles()){
                // recursively call the method for all the subdirectories
                writeFileToTar(f.getAbsolutePath(), entryName+File.separator, tarArchive);
            }
        }
    }
}
