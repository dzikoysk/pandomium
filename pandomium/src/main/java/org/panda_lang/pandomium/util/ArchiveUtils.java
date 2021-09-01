package org.panda_lang.pandomium.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.tukaani.xz.XZInputStream;

import java.io.*;

public class ArchiveUtils {

    public static InputStream unGzip(InputStream gzipArchive) throws IOException {
        return new XZInputStream(gzipArchive);
    }

    public static void unpackTar(InputStream tarArchive, File outputDirectory) throws IOException {
        TarArchiveInputStream tarStream = new TarArchiveInputStream(tarArchive);
        TarArchiveEntry entry;

        while ((entry = tarStream.getNextTarEntry()) != null) {
            unpackTarArchiveEntry(tarStream, entry, outputDirectory);
        }

    }

    public static void unpackTar(TarArchiveInputStream tarStream, TarArchiveEntry[] entries, File outputDirectory) throws IOException {
        for (TarArchiveEntry entry : entries) {
            unpackTarArchiveEntry(tarStream, entry, outputDirectory);
        }
    }

    public static void unpackTarArchiveEntry(TarArchiveInputStream tarStream, TarArchiveEntry entry, File outputDirectory) throws IOException {
        String fileName = entry.getName().substring(entry.getName().indexOf("/"));
        File outputFile = new File(outputDirectory, fileName);

        if (entry.isDirectory()) {
            if (!outputFile.exists()) {
                if (!outputFile.mkdirs()) {
                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
                }
            }

            unpackTar(tarStream, entry.getDirectoryEntries(), outputFile);
            return;
        }

        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            throw new IllegalStateException(String.format("Couldn't create parent directory for %s.", outputFile.getAbsolutePath()));
        }

        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        try (OutputStream outputFileStream = new FileOutputStream(outputFile)) {
            IOUtils.copy(tarStream, outputFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
