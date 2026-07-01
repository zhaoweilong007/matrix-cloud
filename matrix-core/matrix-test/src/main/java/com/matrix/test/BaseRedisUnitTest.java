package com.matrix.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Redis 单元测试基类（基于 Mockito Mock）
 * <p>
 * 使用方式：
 * <pre>
 * public class RedisTest extends BaseRedisUnitTest {
 *     &#64;Test
 *     public void testCacheOperations() {
 *         // 使用 Mockito 模拟 RedisTemplate 操作
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author matrix
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseRedisUnitTest {
}
