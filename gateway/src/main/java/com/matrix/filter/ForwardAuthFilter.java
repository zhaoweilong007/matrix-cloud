package com.matrix.filter;

import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.util.SaTokenConsts;
import com.matrix.config.MatrixSecurityProperties;
import com.matrix.entity.vo.LoginUser;
import com.matrix.utils.LoginHelper;
import com.matrix.utils.ReactiveTenantCTH;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 描述：全局过滤器，为请求添加 Id-Token
 *
 * @author zwl
 * @since 2022/7/8 15:44
 **/

public class ForwardAuthFilter implements GlobalFilter, Ordered {

    private final MatrixSecurityProperties matrixSecurityProperties;

    public ForwardAuthFilter(MatrixSecurityProperties matrixSecurityProperties) {
        this.matrixSecurityProperties = matrixSecurityProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest newRequest = exchange
                .getRequest()
                .mutate()
                // 为请求追加 Id-Token 参数
                .header(SaIdUtil.ID_TOKEN, SaIdUtil.getToken())
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        // 忽略白名单地址
        if (SaRouter.match(matrixSecurityProperties.getWhiteUrls()).isHit()) {
            return chain.filter(newExchange).contextWrite(context -> {
                ReactiveTenantCTH.setignore(context);
                return context;
            });
        }
        LoginUser loginUser = LoginHelper.getLoginUser();
        return chain.filter(newExchange).contextWrite(context -> {
            ReactiveTenantCTH.put(context, loginUser.getTenantId());
            return context;
        });
    }

    @Override
    public int getOrder() {
        return SaTokenConsts.ASSEMBLY_ORDER + 1;
    }
}
