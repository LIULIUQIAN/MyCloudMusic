package com.example.mycloudmusic.util;

import org.junit.Test;

import static org.junit.Assert.*;


public class StringUtilTest {

    @Test
    public void testIsPhone(){

        assertTrue(StringUtil.isPhone("13044126666"));

        assertFalse(StringUtil.isPhone("123456789011"));

        assertFalse(StringUtil.isPhone("1502199494"));
    }
}
