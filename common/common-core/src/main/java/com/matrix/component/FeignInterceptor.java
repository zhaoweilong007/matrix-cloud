package com.matrix.component;

import cn.dev33.satoken.id.SaIdUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 描述：feign拦截器, 为 Feign 的 RCP调用 添加请求头Id-Token
 *
 * @author zwl
 * @since 2022/7/8 15:55
 **/
public class FeignInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {
        template.header(SaIdUtil.ID_TOKEN, SaIdUtil.getToken());
    }
}
