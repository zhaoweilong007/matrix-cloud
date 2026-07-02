package com.matrix.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

/**
 * H2 内存数据库测试基类。
 *
 * <p>自动在每个测试方法后执行 {@code /sql/clean.sql} 清理脚本，
 * 防止测试数据污染。子类可通过覆盖 {@code @Sql} 注解自定义清理行为。</p>
 *
 * <p>使用方式：
 * <pre>{@code
 * @SpringBootTest
 * class UserMapperTest extends BaseDbUnitTest {
 *     @Autowired
 *     private UserMapper userMapper;
 *
 *     @Test
 *     void testSelectById() {
 *         User user = userMapper.selectById(1L);
 *         assertNotNull(user);
 *     }
 * }
 * }</pre></p>
 */
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public abstract class BaseDbUnitTest {
}
