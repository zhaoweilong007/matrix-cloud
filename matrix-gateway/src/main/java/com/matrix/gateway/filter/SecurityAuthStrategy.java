package com.matrix.gateway.filter;

import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.matrix.auto.properties.IgnoreWhiteProperties;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.enums.PlatformUserTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * 针对老版token访问新版接口 按照请求头定义类型鉴权
 * 安全认证策略
 *
 * @author ZhaoWeiLong
 * @since 2023/7/12
 **/
@Slf4j
@RequiredArgsConstructor
public class SecurityAuthStrategy implements SaFilterAuthStrategy {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final IgnoreWhiteProperties ignoreWhite;

    @Override
    public void run(Object o) {
        //增加断点认证
        if (SaRouter.match("/actuator/**").isHit()) {
            SaBasicUtil.check();
            return;
        }
        ServerWebExchange exchange = SaReactorSyncHolder.getContext();
        // 登录校验 -- 拦截所有路由

        //是否白名单 如白名单包含token 也进行校验 校验不通过也放行
        if (SaRouter.match(ignoreWhite.getWhites()).isHit()) {
            try {
                final String tokenValue = exchange.getRequest().getHeaders().getFirst(CommonConstants.TOKEN_HEADER);
                if (tokenValue != null) {
                    checkAuth(exchange);
                }
            } catch (Exception ignore) {
            }
            return;
        }

        //兜底校验
        checkAuth(exchange);
    }

    private void checkAuth(ServerWebExchange exchange) {
        // 检查是否登录 是否有token
        StpUtil.checkLogin();
        //获取用户拥有资源
        List<String> permissionList = StpUtil.getPermissionList();
        //将访问所需资源或用户拥有资源进行比对
        ServerHttpRequest request = SaReactorSyncHolder.getContext().getRequest();
        String path = request.getURI().getPath();
        String resource = permissionList.stream().filter(url -> antPathMatcher.match(url, path)).findFirst().orElseThrow(() -> new NotPermissionException(path));
        log.info("用户:【{}】 资源:【{}】授权成功", StpUtil.getLoginId(), resource);

        //设置用户类型
        final ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                .header(CommonConstants.USER_TYPE, PlatformUserTypeEnum.SYS_USER.name())
                .build();
        exchange = exchange.mutate().request(httpRequest).build();
    }
}
