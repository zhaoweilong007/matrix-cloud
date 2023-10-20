package com.matrix.datapermission.test.rule;

import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.common.enums.RoleEnum;
import com.matrix.common.model.RoleDTO;
import com.matrix.common.model.login.LoginUser;
import com.matrix.datapermission.rule.UserDataPermissionRule;
import com.matrix.test.base.BaseMockitoUnitTest;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.List;

import static com.matrix.test.util.RandomUtils.randomPojo;
import static com.matrix.test.util.RandomUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/26
 **/
public class UserDataPermissionRuleTest extends BaseMockitoUnitTest {


    @InjectMocks
    UserDataPermissionRule rule;


    @BeforeEach
    public void setUp() {
        rule.getTableNames().clear();
    }

    @Test // 无 LoginUser
    public void testGetExpression_noLoginUser() {
        // 准备参数
        String tableName = randomString();
        Alias tableAlias = new Alias(randomString());
        // mock 方法

        // 调用
        Expression expression = rule.getExpression(tableName, tableAlias);
        // 断言
        assertNull(expression);
    }


    @Test // 无角色
    public void testGetExpression_noRole() {
        try (MockedStatic<LoginUserContextHolder> loginUserMock
                     = mockStatic(LoginUserContextHolder.class);

             MockedStatic<TerminalContextHolder> teiminalMock
                     = mockStatic(TerminalContextHolder.class);
        ) {
            // 准备参数
            String tableName = "sys_user";
            Alias tableAlias = new Alias("u");
            // mock 方法
            LoginUser loginUser = randomPojo(LoginUser.class, o -> o.setTenantId(1L).setRoles(Collections.emptyList()));
            loginUserMock.when(LoginUserContextHolder::getUser).thenReturn(loginUser);
            teiminalMock.when(TerminalContextHolder::getUserType).thenReturn(PlatformUserTypeEnum.SYS_USER);
            // 调用
            final Expression expression = rule.getExpression(tableName, tableAlias);

            // 断言
            assertNull(expression);
        }
    }


    @Test // 是admin角色
    public void testGetExpression_isAdminRole() {
        try (MockedStatic<LoginUserContextHolder> loginUserMock
                     = mockStatic(LoginUserContextHolder.class);

             MockedStatic<TerminalContextHolder> teiminalMock
                     = mockStatic(TerminalContextHolder.class);
        ) {
            // 准备参数
            String tableName = "sys_user";
            Alias tableAlias = new Alias("u");
            // mock 方法
            final RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleKey(RoleEnum.ADMIN.getRoleKey());
            LoginUser loginUser = randomPojo(LoginUser.class, o -> o.setTenantId(1L).setRoles(List.of(roleDTO)));
            loginUserMock.when(LoginUserContextHolder::getUser).thenReturn(loginUser);
            teiminalMock.when(TerminalContextHolder::getUserType).thenReturn(PlatformUserTypeEnum.SYS_USER);
            // 调用
            final Expression expression = rule.getExpression(tableName, tableAlias);

            // 断言
            assertNull(expression);
        }
    }

    @Test // 其他角色情况下
    public void testGetExpression_isOtherRole() {
        try (MockedStatic<LoginUserContextHolder> loginUserMock
                     = mockStatic(LoginUserContextHolder.class);

             MockedStatic<TerminalContextHolder> teiminalMock
                     = mockStatic(TerminalContextHolder.class);
        ) {
            // 准备参数
            String tableName = "sys_user";
            Alias tableAlias = new Alias("u");
            // mock 方法
            final RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleKey(RoleEnum.BROKER.getRoleKey());
            LoginUser loginUser = randomPojo(LoginUser.class, o -> o.setTenantId(1L).setRoles(List.of(roleDTO)));
            loginUserMock.when(LoginUserContextHolder::getUser).thenReturn(loginUser);
            teiminalMock.when(TerminalContextHolder::getUserType).thenReturn(PlatformUserTypeEnum.SYS_USER);
            // 调用
            // 添加 table配置
            rule.addTable("sys_user");

            final Expression expression = rule.getExpression(tableName, tableAlias);

            // 断言
            assertEquals("u.tenant_id = 1", expression.toString());
        }
    }


}
