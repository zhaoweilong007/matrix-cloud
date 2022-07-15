package com.matrix.component;

import cn.dev33.satoken.stp.StpUtil;
import com.matrix.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/15 15:44
 **/
@Component
public class UserServiceImpl implements UserService {
    @Override
    public String getCurrentUserId() {
        String loginId = null;
        try {
            loginId = StpUtil.getLoginId().toString();
        } catch (Exception ignored) {

        }
        return Optional.ofNullable(loginId).orElse("1");
    }
}
