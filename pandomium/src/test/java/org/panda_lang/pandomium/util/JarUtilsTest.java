package org.panda_lang.pandomium.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

class JarUtilsTest{
    @Test
    void testFindVersion() throws IOException {
        Assertions.assertEquals("1.0.0", extractVersion("<version>1.0.0</version>"));
    }

    private String extractVersion(String input) throws IOException {
        try(BufferedReader bw = new BufferedReader(new StringReader(input))){
            String line;
            int startIndex;
            int stopIndex;
            while ((line = bw.readLine()) != null) {
                startIndex = line.indexOf("<version>");
                stopIndex = line.indexOf("</version>");
                if (startIndex != -1 && stopIndex != -1){
                    return line.substring(startIndex+9, stopIndex);
                }
            }
        }
        return null;
    }
}