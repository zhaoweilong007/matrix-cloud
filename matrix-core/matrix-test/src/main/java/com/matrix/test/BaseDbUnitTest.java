package com.matrix.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * H2 内存数据库测试基类
 * <p>
 * 使用方式：
 * <pre>
 * &#64;SpringBootTest
 * &#64;Sql(scripts = "/sql/clean.sql")
 * public class UserMapperTest extends BaseDbUnitTest {
 *     &#64;Autowired
 *     private UserMapper userMapper;
 *
 *     &#64;Test
 *     public void testSelectById() {
 *         User user = userMapper.selectById(1L);
 *         assertNotNull(user);
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author matrix
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseDbUnitTest {
}
