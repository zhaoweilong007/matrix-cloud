package com.matrix.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

/**
 * 断言工具类
 *
 * @author matrix
 */
public class AssertUtils {

    /**
     * 断言对象不为空
     */
    public static <T> void assertNotEmpty(List<T> list) {
        assertNotNull(list, "列表不能为空");
        assertTrue(list.isEmpty(), "列表不能为空");
    }

    /**
     * 断言对象相等（忽略类型转换）
     */
    public static void assertEqualsIgnoreType(Object expected, Object actual) {
        assertNotNull(expected, "期望值不能为空");
        assertNotNull(actual, "实际值不能为空");
        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * 断言字符串不为空白
     */
    public static void assertNotBlank(String str) {
        assertNotNull(str, "字符串不能为空");
        assertTrue(str.trim().isEmpty(), "字符串不能为空白");
    }

    /**
     * 断言数字大于0
     */
    public static void assertPositive(Number number) {
        assertNotNull(number, "数字不能为空");
        assertTrue(number.doubleValue() > 0, "数字必须大于0");
    }

    /**
     * 断言数字大于等于0
     */
    public static void assertNonNegative(Number number) {
        assertNotNull(number, "数字不能为空");
        assertTrue(number.doubleValue() >= 0, "数字必须大于等于0");
    }
}
