package com.example.mycloudmusic.util;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void testFormatFileSize(){

        assertEquals(FileUtil.formatFileSize(1),"1.00Byte");

        assertEquals(FileUtil.formatFileSize(1234),"1.21K");

        assertNotEquals(FileUtil.formatFileSize(1234), "1K");

    }
}
