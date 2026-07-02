package com.matrix.test.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.common.enums.CommonStatusEnum;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * 随机工具类
 */
public class RandomUtils {

    /** 随机字符串默认长度 */
    private static final int RANDOM_STRING_LENGTH = 10;

    /** tinyint 类型最大值 */
    private static final int TINYINT_MAX = 127;

    /** 随机日期最大偏移天数 */
    private static final int RANDOM_DATE_MAX = 30;

    /** 随机集合默认大小 */
    private static final int RANDOM_COLLECTION_LENGTH = 5;

    /** Podam 工厂实例，用于生成随机 POJO */
    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    static {
        // 字符串
        PODAM_FACTORY
                .getStrategy()
                .addOrReplaceTypeManufacturer(
                        String.class, (dataProviderStrategy, attributeMetadata, map) -> randomString());
        // Integer
        PODAM_FACTORY
                .getStrategy()
                .addOrReplaceTypeManufacturer(Integer.class, (dataProviderStrategy, attributeMetadata, map) -> {
                    // 如果是 status 的字段，返回 0 或 1
                    if ("status".equals(attributeMetadata.getAttributeName())) {
                        return RandomUtil.randomEle(CommonStatusEnum.values()).getStatus();
                    }
                    // 如果是 type、status 结尾的字段，返回 tinyint 范围
                    if (StrUtil.endWithAnyIgnoreCase(
                            attributeMetadata.getAttributeName(), "type", "status", "category", "scope", "result")) {
                        return RandomUtil.randomInt(0, TINYINT_MAX + 1);
                    }
                    return RandomUtil.randomInt();
                });
        // Boolean
        PODAM_FACTORY
                .getStrategy()
                .addOrReplaceTypeManufacturer(Boolean.class, (dataProviderStrategy, attributeMetadata, map) -> {
                    // 如果是 deleted 的字段，返回非删除
                    if ("deleted".equals(attributeMetadata.getAttributeName())) {
                        return false;
                    }
                    return RandomUtil.randomBoolean();
                });
    }

    /**
     * 生成随机字符串
     *
     * @return 随机字符串
     */
    public static String randomString() {
        return RandomUtil.randomString(RANDOM_STRING_LENGTH);
    }

    /**
     * 生成随机 Long 类型 ID
     *
     * @return 随机 Long
     */
    public static Long randomLongId() {
        return RandomUtil.randomLong(0, Long.MAX_VALUE);
    }

    /**
     * 生成随机 Integer
     *
     * @return 随机 Integer
     */
    public static Integer randomInteger() {
        return RandomUtil.randomInt(0, Integer.MAX_VALUE);
    }

    /**
     * 生成随机 Date
     *
     * @return 随机 Date
     */
    public static Date randomDate() {
        return RandomUtil.randomDay(0, RANDOM_DATE_MAX);
    }

    /**
     * 生成随机 LocalDateTime
     *
     * @return 随机 LocalDateTime
     */
    public static LocalDateTime randomLocalDateTime() {
        return LocalDateTimeUtil.of(randomDate());
    }

    /**
     * 生成随机 Short
     *
     * @return 随机 Short
     */
    public static Short randomShort() {
        return (short) RandomUtil.randomInt(0, Short.MAX_VALUE);
    }

    /**
     * 生成随机集合
     *
     * @param clazz 集合元素类型
     * @param <T>   元素泛型类型
     * @return 随机 Set 集合
     */
    public static <T> Set<T> randomSet(Class<T> clazz) {
        return Stream.iterate(0, i -> i)
                .limit(RandomUtil.randomInt(1, RANDOM_COLLECTION_LENGTH))
                .map(i -> randomPojo(clazz))
                .collect(Collectors.toSet());
    }

    /**
     * 生成随机通用状态（启用/禁用）
     *
     * @return 状态值，0 或 1
     */
    public static Integer randomCommonStatus() {
        return RandomUtil.randomEle(CommonStatusEnum.values()).getStatus();
    }

    /**
     * 生成随机邮箱地址
     *
     * @return 随机邮箱
     */
    public static String randomEmail() {
        return randomString() + "@qq.com";
    }

    /**
     * 生成随机 POJO 对象
     *
     * @param clazz     POJO 类型
     * @param consumers 自定义回调，用于进一步设置对象属性
     * @param <T>       POJO 泛型类型
     * @return 随机生成的 POJO 对象
     */
    @SafeVarargs
    public static <T> T randomPojo(Class<T> clazz, Consumer<T>... consumers) {
        T pojo = PODAM_FACTORY.manufacturePojo(clazz);
        // 非空时，回调逻辑。通过它，可以实现 Pojo 的进一步处理
        if (ArrayUtil.isNotEmpty(consumers)) {
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(pojo));
        }
        return pojo;
    }

    /**
     * 生成随机 POJO 对象（支持泛型类型参数）
     *
     * @param clazz     POJO 类型
     * @param type      泛型类型参数
     * @param consumers 自定义回调，用于进一步设置对象属性
     * @param <T>       POJO 泛型类型
     * @return 随机生成的 POJO 对象
     */
    @SafeVarargs
    public static <T> T randomPojo(Class<T> clazz, Type type, Consumer<T>... consumers) {
        T pojo = PODAM_FACTORY.manufacturePojo(clazz, type);
        // 非空时，回调逻辑。通过它，可以实现 Pojo 的进一步处理
        if (ArrayUtil.isNotEmpty(consumers)) {
            Arrays.stream(consumers).forEach(consumer -> consumer.accept(pojo));
        }
        return pojo;
    }

    /**
     * 生成随机 POJO 对象列表
     *
     * @param clazz     POJO 类型
     * @param consumers 自定义回调，用于进一步设置对象属性
     * @param <T>       POJO 泛型类型
     * @return 随机生成的 POJO 对象列表
     */
    @SafeVarargs
    public static <T> List<T> randomPojoList(Class<T> clazz, Consumer<T>... consumers) {
        int size = RandomUtil.randomInt(1, RANDOM_COLLECTION_LENGTH);
        return Stream.iterate(0, i -> i)
                .limit(size)
                .map(o -> randomPojo(clazz, consumers))
                .collect(Collectors.toList());
    }
}
