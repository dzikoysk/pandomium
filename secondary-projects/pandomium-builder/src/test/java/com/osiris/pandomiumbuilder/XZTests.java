package com.osiris.pandomiumbuilder;

import com.osiris.pandomiumbuilder.tar.TarXz;
import org.junit.jupiter.api.Test;

import java.io.*;

public class XZTests {

    @Test
    void createXZTar() throws IOException {
        File testText = new File(System.getProperty("user.dir")+"/test.txt");
        if (!testText.exists()) testText.createNewFile();
        File testText2 = new File(System.getProperty("user.dir")+"/test2.txt");
        if (!testText2.exists()) testText2.createNewFile();

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(testText));
            for (int i = 0; i < 100000; i++) {
                bw.write("Hellovh9du89h8989C)?c9ßaa9ßc9uCU)B(§?!(?!§§U(( U ? !( !R( ?!§ ?ÜR!§ !R§ U§!R *!§R * !§R*!R § Z!§R!( RT( W(T DTD//D/! §//!\n");
            }
            bw.close();
        } catch (IOException e) {
            throw e;
        }

        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(testText2));
            for (int i = 0; i < 100000; i++) {
                bw.write("Hellovh9du89h8989C)?c9ßaa9ßc9uCU)B(§?!(?!§§U(( U ? !( !R( ?!§ ?ÜR!§ !R§ U§!R *!§R * !§R*!R § Z!§R!( RT( W(T DTD//D/! §//!\n");
            }
            bw.close();
        } catch (IOException e) {
            throw e;
        }

        new TarXz("test")
                .addFile(testText, testText2)
                .createInDir(System.getProperty("user.dir"));
    }
}
