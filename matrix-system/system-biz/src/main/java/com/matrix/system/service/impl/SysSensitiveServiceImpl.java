package com.matrix.system.service.impl;

import com.matrix.common.model.login.LoginUser;
import com.matrix.common.service.ISensitiveService;
import com.matrix.auth.utils.LoginHelper;
import org.springframework.stereotype.Service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 */
@Service
public class SysSensitiveServiceImpl implements ISensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        return loginUser == null || !"admin".equals(loginUser.getUsername());
    }

}
