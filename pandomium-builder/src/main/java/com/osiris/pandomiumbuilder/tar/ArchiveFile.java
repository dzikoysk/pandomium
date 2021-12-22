package com.osiris.pandomiumbuilder.tar;

import java.io.File;
import java.io.IOException;

public interface ArchiveFile {

    default File createInCurrentDir() throws IOException {
        return createInDir(new File(System.getProperty("user.dir")));
    }
    default File unpackInCurrentDir(){
        return unpackInDir(new File(System.getProperty("user.dir")));
    }

    default File createInDir(String dirPath) throws IOException {
        return createInDir(new File(dirPath));
    }
    default File unpackInDir(String dirPath){
        return unpackInDir(dirPath);
    }

    File createInDir(File dir) throws IOException;
    File unpackInDir(File dir);

}
