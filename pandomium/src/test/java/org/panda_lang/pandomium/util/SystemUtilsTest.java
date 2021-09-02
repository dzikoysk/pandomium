package org.panda_lang.pandomium.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemUtilsTest {

    @Test
    void testInjectLib() throws Exception {
        String libPath = "C:/MyPath";
        SystemUtils.injectLibraryPath(libPath);
        assertTrue(System.getProperty("java.library.path").contains(libPath));
    }
}