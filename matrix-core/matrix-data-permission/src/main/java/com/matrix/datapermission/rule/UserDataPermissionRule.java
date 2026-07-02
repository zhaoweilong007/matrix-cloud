package com.matrix.datapermission.rule;

import com.matrix.common.model.login.LoginUser;

/**
 * 用户数据规则 (只能查看自己本人数据）
 *
 **/
public class UserDataPermissionRule extends AbstractDataPermissionRule {

    public static final String USER_COLUMN = "created_by";

    @Override
    public String getColumnName() {
        return USER_COLUMN;
    }

    @Override
    protected Long getColumnValue(LoginUser loginUser) {
        return loginUser.getUserId();
    }
}
