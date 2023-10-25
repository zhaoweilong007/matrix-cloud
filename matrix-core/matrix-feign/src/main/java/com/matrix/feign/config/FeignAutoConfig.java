package com.matrix.feign.config;

import cn.dev33.satoken.same.SaSameUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.common.request.CacheRequestBodyWrapper;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoWeiLong
 * @since 2023/6/9
 **/
@Configuration
public class FeignAutoConfig {

    private final List<String> requestHeaders = new ArrayList<>();
    private final String[] array;

    {
        requestHeaders.add(CommonConstants.USER_ID_HEADER);
        requestHeaders.add(CommonConstants.VERSION_HEADER);
        requestHeaders.add(CommonConstants.TOKEN_HEADER);
        array = requestHeaders.toArray(new String[]{});
    }

    /**
     * 使用feign client发送请求时，传递tenantId和traceId
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            //服务内部调用携带token
            requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
            //传递tenantId
            Long tenantId = TenantContextHolder.getTenantId();
            if (tenantId != null) {
                requestTemplate.header(CommonConstants.TENANT_ID_HEADER, String.valueOf(tenantId));
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                final HttpServletRequest httpServletRequest = attributes.getRequest();
                if (httpServletRequest instanceof CacheRequestBodyWrapper request) {
                    final Map<String, String> headerMap = request.getHeaderMap();
                    headerMap.forEach((headerName, headerValue) -> {
                        String header = StrUtil.getContainsStrIgnoreCase(headerName, array);
                        if (header != null) {
                            requestTemplate.header(header, headerValue);
                        }
                    });
                } else {
                    Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
                    if (headerNames != null) {
                        String headerName;
                        String headerValue;
                        while (headerNames.hasMoreElements()) {
                            headerName = headerNames.nextElement();
                            String header = StrUtil.getContainsStrIgnoreCase(headerName, array);
                            if (header != null) {
                                headerValue = httpServletRequest.getHeader(headerName);
                                requestTemplate.header(header, headerValue);
                            }
                        }
                    }
                }
            }
        };
    }
}
