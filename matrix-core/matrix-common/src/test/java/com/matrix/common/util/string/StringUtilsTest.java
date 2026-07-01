package com.matrix.common.util.string;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * StringUtils 单元测试
 */
class StringUtilsTest {

    @Test
    void testBlankToDefault() {
        assertEquals("default", StringUtils.blankToDefault(null, "default"));
        assertEquals("default", StringUtils.blankToDefault("", "default"));
        assertEquals("default", StringUtils.blankToDefault("  ", "default"));
        assertEquals("value", StringUtils.blankToDefault("value", "default"));
    }

    @Test
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("value"));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(StringUtils.isNotEmpty(null));
        assertFalse(StringUtils.isNotEmpty(""));
        assertTrue(StringUtils.isNotEmpty(" "));
        assertTrue(StringUtils.isNotEmpty("value"));
    }

    @Test
    void testTrim() {
        assertNull(StringUtils.trim(null));
        assertEquals("", StringUtils.trim(""));
        assertEquals("value", StringUtils.trim("  value  "));
    }

    @Test
    void testSubstring() {
        assertEquals("llo", StringUtils.substring("hello", 2));
        assertEquals("ell", StringUtils.substring("hello", 1, 4));
    }

    @Test
    void testFormat() {
        assertEquals("hello world", StringUtils.format("hello {}", "world"));
        assertEquals("a and b", StringUtils.format("{} and {}", "a", "b"));
    }

    @Test
    void testStr2Set() {
        Set<String> result = StringUtils.str2Set("a,b,c", ",");
        assertEquals(3, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    void testStr2List() {
        List<String> result = StringUtils.str2List("a,b,c", ",", true, true);
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    void testContainsAnyIgnoreCase() {
        assertTrue(StringUtils.containsAnyIgnoreCase("hello", "HELLO", "world"));
        assertFalse(StringUtils.containsAnyIgnoreCase("hello", "WORLD", "JAVA"));
    }

    @Test
    void testToUnderScoreCase() {
        assertEquals("hello_world", StringUtils.toUnderScoreCase("helloWorld"));
        assertEquals("user_name", StringUtils.toUnderScoreCase("userName"));
    }

    @Test
    void testInStringIgnoreCase() {
        assertTrue(StringUtils.inStringIgnoreCase("hello", "HELLO", "world"));
        assertFalse(StringUtils.inStringIgnoreCase("hello", "WORLD", "JAVA"));
    }

    @Test
    void testConvertToCamelCase() {
        assertEquals("HelloWorld", StringUtils.convertToCamelCase("HELLO_WORLD"));
        assertEquals("UserName", StringUtils.convertToCamelCase("USER_NAME"));
    }

    @Test
    void testToCamelCase() {
        assertEquals("helloWorld", StringUtils.toCamelCase("hello_world"));
        assertEquals("userName", StringUtils.toCamelCase("user_name"));
    }

    @Test
    void testIsMatch() {
        assertTrue(StringUtils.isMatch("/api/**", "/api/users"));
        assertTrue(StringUtils.isMatch("/api/*", "/api/users"));
        assertFalse(StringUtils.isMatch("/api/*", "/api/users/1"));
        assertTrue(StringUtils.isMatch("/api/**", "/api/users/1"));
    }

    @Test
    void testPadl() {
        assertEquals("00123", StringUtils.padl(123, 5));
        assertEquals("12345", StringUtils.padl(12345, 5));
        assertEquals("34567", StringUtils.padl(1234567, 5));
    }

    @Test
    void testSplitList() {
        List<String> result = StringUtils.splitList("a,b,c");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    void testSplitListWithSeparator() {
        List<String> result = StringUtils.splitList("a|b|c", "|");
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("c", result.get(2));
    }

    @Test
    void testSplitTo() {
        List<String> result = StringUtils.splitTo("1,2,3", Object::toString);
        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }
}
