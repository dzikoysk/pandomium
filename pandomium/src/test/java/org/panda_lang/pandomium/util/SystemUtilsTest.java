package org.panda_lang.pandomium.util;

import net.dzikoysk.dynamiclogger.backend.PrintStreamLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemUtilsTest {

    @Test
    void testInjectLib() throws Exception {
        String libPath = "C:/MyPath";
        SystemUtils.injectLibraryPath(new PrintStreamLogger(System.out, System.err), libPath);
        assertTrue(System.getProperty("java.library.path").contains(libPath));
    }

}