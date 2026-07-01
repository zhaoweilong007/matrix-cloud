package com.matrix.test;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 随机对象生成工具
 * <p>
 * 基于 PODAM 库，自动生成随机 POJO 对象，用于单元测试
 * </p>
 *
 * @author matrix
 */
public class RandomUtils {

    private static final PodamFactory FACTORY = new PodamFactoryImpl();

    /**
     * 生成随机对象
     *
     * @param clazz 目标类
     * @param <T>   类型
     * @return 随机对象
     */
    public static <T> T randomPojo(Class<T> clazz) {
        return FACTORY.manufacturePojo(clazz);
    }

    /**
     * 生成随机对象列表
     *
     * @param clazz 目标类
     * @param count 数量
     * @param <T>   类型
     * @return 随机对象列表
     */
    public static <T> List<T> randomPojoList(Class<T> clazz, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> FACTORY.manufacturePojo(clazz))
                .collect(Collectors.toList());
    }

    /**
     * 生成随机 String
     */
    public static String randomString() {
        return FACTORY.manufacturePojo(String.class);
    }

    /**
     * 生成随机 Long
     */
    public static Long randomLong() {
        return FACTORY.manufacturePojo(Long.class);
    }

    /**
     * 生成随机 Integer
     */
    public static Integer randomInteger() {
        return FACTORY.manufacturePojo(Integer.class);
    }
}
