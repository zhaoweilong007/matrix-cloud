package com.matrix.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 纯 Mockito 单元测试基类
 * <p>
 * 使用方式：
 * <pre>
 * public class UserServiceTest extends BaseMockitoUnitTest {
 *     &#64;Mock
 *     private UserMapper userMapper;
 *
 *     &#64;InjectMocks
 *     private UserServiceImpl userService;
 *
 *     &#64;Test
 *     public void testGetUser() {
 *         when(userMapper.selectById(1L)).thenReturn(new User());
 *         User user = userService.getUserById(1L);
 *         assertNotNull(user);
 *     }
 * }
 * </pre>
 * </p>
 *
 * @author matrix
 */
@ExtendWith(MockitoExtension.class)
public abstract class BaseMockitoUnitTest {
}
