package com.matrix.security;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述：权限鉴权策略
 *
 * @author zwl
 * @since 2022/7/8 15:04
 **/
@Component
@Slf4j
public class SecurityAuth implements SaFilterAuthStrategy {


    @Override
    public void run(Object r) {
        log.info("SecurityAuth invoke ....:{}", r);
    }
}
