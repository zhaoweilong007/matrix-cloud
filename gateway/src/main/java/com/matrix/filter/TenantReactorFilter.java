package com.matrix.filter;

import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.util.SaTokenConsts;
import com.matrix.context.TenantContextHold;
import com.matrix.context.UserContextHolder;
import com.matrix.entity.vo.LoginUser;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/29 10:53
 **/
@AllArgsConstructor
@Order(SaTokenConsts.ASSEMBLY_ORDER + 1)
@EnableConfigurationProperties(TenantProperties.class)
public class TenantReactorFilter implements WebFilter {

    private TenantProperties tenantProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!tenantProperties.getEnable()) {
            return chain.filter(exchange);
        }
        if (SaRouter.match(tenantProperties.getIgnoreUrls()).isHit()) {
            TenantContextHold.setIgnore(true);
        } else {
            LoginUser loginUser = LoginHelper.getLoginUser();
            UserContextHolder.setLoginUser(loginUser);
            TenantContextHold.setTenantId(loginUser.getTenantId());
        }
        return chain.filter(exchange).doFinally(signalType -> {
            TenantContextHold.clear();
            UserContextHolder.clear();
        });
    }

}
