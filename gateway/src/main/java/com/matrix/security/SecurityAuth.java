package com.matrix.security;

import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SecurityAuth implements SaFilterAuthStrategy {


    @Override
    public void run(Object r) {
        log.info("SecurityAuth invoke ....:{}", r);
        //检查登录
        StpUtil.checkLogin();

        //获取用户拥有资源


        //将访问所需资源或用户拥有资源进行比对



    }
}
