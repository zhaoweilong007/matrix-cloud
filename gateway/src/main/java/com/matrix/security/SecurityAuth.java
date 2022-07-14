package com.matrix.security;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.stp.StpUtil;
import com.matrix.service.SysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;

/**
 * 描述：权限鉴权策略
 *
 * @author zwl
 * @since 2022/7/8 15:04
 **/
@Component
@Slf4j
public class SecurityAuth implements SaFilterAuthStrategy {


    @Autowired
    private SysResourceService sysResourceService;


    @Override
    public void run(Object r) {
        log.info("SecurityAuth check permission...");
        //检查登录
        StpUtil.checkLogin();

        //获取用户拥有资源
        List<String> permissionList = StpUtil.getPermissionList();

        //将访问所需资源或用户拥有资源进行比对
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        List<String> resourceUrls = sysResourceService.getAllResource();
        ServerHttpRequest request = SaReactorSyncHolder.getContext().getRequest();
        String path = request.getURI().getPath();
        Optional<String> optional = resourceUrls.stream().filter(url -> antPathMatcher.match(url, path)).findFirst();

        optional.filter(permissionList::contains).ifPresent(permission -> {
            log.info("用户{}资源授权成功:{}", StpUtil.getLoginId(), permission);
        });

        throw new NotPermissionException(path);
        //没有匹配则直接拒绝访问
    }
}
