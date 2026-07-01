package com.matrix.gateway.filter;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.filter.SaFilterAuthStrategy;
import cn.dev33.satoken.httpauth.basic.SaHttpBasicUtil;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.matrix.auto.properties.IgnoreWhiteProperties;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.gateway.utils.WebFrameworkUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

/**
 * 安全认证策略。
 *
 * <p>包含：登录校验、权限匹配、伪造请求头防御、客户端绑定校验、客户端访问路径/IP白名单校验。</p>
 *
 * @author ZhaoWeiLong
 * @since 2023/7/12
 **/
@Slf4j
@RequiredArgsConstructor
public class SecurityAuthStrategy implements SaFilterAuthStrategy {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final IgnoreWhiteProperties ignoreWhite;

    /** Sa-Token 客户端标识 key */
    private static final String CLIENT_KEY = "clientId";
    /** 客户端访问路径 key */
    private static final String CLIENT_ACCESS_PATH_KEY = "clientAccessPath";
    /** 客户端 IP 白名单 key */
    private static final String CLIENT_IP_WHITELIST_KEY = "clientIpWhitelist";

    @Override
    public void run(Object o) {
        // 增加断点认证（Basic Auth）
        if (SaRouter.match("/actuator/**").isHit()) {
            SaHttpBasicUtil.check();
            return;
        }
        ServerWebExchange exchange = SaReactorSyncHolder.getExchange();

        // P0-1: 移除伪造的 login-user 请求头，防止下游服务被伪造信息攻击
        exchange = removeLoginUserHeader(exchange);

        // 是否白名单 — 如白名单包含 token，也进行校验（校验不通过也放行）
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

        // 兜底校验
        checkAuth(exchange);
    }

    private void checkAuth(ServerWebExchange exchange) {
        // 检查是否登录
        StpUtil.checkLogin();

        // P0-2: 客户端 ID 与 Token 绑定校验，防止 Token 跨客户端滥用
        validateClientBinding(exchange);

        // P0-3: 客户端访问路径 + IP 白名单校验
        validateClientAccessRules(exchange);

        // 获取用户拥有资源，与请求路径匹配
        List<String> permissionList = StpUtil.getPermissionList();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String resource = permissionList.stream()
                .filter(url -> antPathMatcher.match(url, path))
                .findFirst()
                .orElseThrow(() -> new NotPermissionException(path));
        log.info("用户:【{}】 资源:【{}】授权成功", StpUtil.getLoginId(), resource);

        // 设置用户类型
        final ServerHttpRequest httpRequest = request.mutate()
                .header(CommonConstants.USER_TYPE, PlatformUserTypeEnum.SYS_USER.name())
                .build();
        SaReactorSyncHolder.getExchange().mutate().request(httpRequest);
    }

    /** P0-1: 移除伪造的 login-user、login-user-id 等认证请求头 */
    private ServerWebExchange removeLoginUserHeader(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        if (headers.getFirst("login-user") != null
                || headers.getFirst("login-user-id") != null
                || headers.getFirst("login-user-type") != null) {
            ServerHttpRequest cleaned = request.mutate()
                    .headers(h -> {
                        h.remove("login-user");
                        h.remove("login-user-id");
                        h.remove("login-user-type");
                    }).build();
            return exchange.mutate().request(cleaned).build();
        }
        return exchange;
    }

    /** P0-2: 客户端 ID 与 Token 绑定校验 */
    private void validateClientBinding(ServerWebExchange exchange) {
        String headerCid = exchange.getRequest().getHeaders().getFirst(CLIENT_KEY);
        String paramCid = exchange.getRequest().getQueryParams().getFirst(CLIENT_KEY);
        Object extra = StpUtil.getExtra(CLIENT_KEY);
        String tokenClientId = extra == null ? null : extra.toString();
        if (tokenClientId != null && !tokenClientId.equals(headerCid)
                && !tokenClientId.equals(paramCid)) {
            throw new NotPermissionException("客户端ID与Token不匹配");
        }
    }

    /** P0-3: 客户端访问路径 + IP 白名单校验 */
    private void validateClientAccessRules(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();

        String accessPath = getTokenExtra(CLIENT_ACCESS_PATH_KEY);
        if (accessPath != null && !accessPath.isEmpty()) {
            List<String> accessPathList = List.of(accessPath.split("[,;\\r\\n]+"));
            boolean matched = accessPathList.stream()
                    .anyMatch(rule -> antPathMatcher.match(rule.trim(), path));
            if (!matched) {
                throw new NotPermissionException("当前客户端未授权访问该接口路径");
            }
        }

        String ipWhitelist = getTokenExtra(CLIENT_IP_WHITELIST_KEY);
        if (ipWhitelist != null && !ipWhitelist.isEmpty()) {
            String clientIp = WebFrameworkUtils.getClientIP(exchange);
            List<String> ipList = List.of(ipWhitelist.split("[,;\\r\\n]+"));
            boolean ipMatched = ipList.stream()
                    .anyMatch(rule -> isIpMatch(rule.trim(), clientIp));
            if (!ipMatched) {
                throw new NotPermissionException("当前客户端IP不在白名单内");
            }
        }
    }

    private String getTokenExtra(String key) {
        Object extra = StpUtil.getExtra(key);
        return extra == null ? null : extra.toString();
    }

    /** 简单 IP 匹配（支持 CIDR 前缀匹配和精确 IP） */
    private boolean isIpMatch(String rule, String ip) {
        if (ip == null) return false;
        if (rule.equals(ip)) return true;
        if (rule.contains("/")) {
            return ip.startsWith(rule.substring(0, rule.indexOf('/')));
        }
        return false;
    }
}
