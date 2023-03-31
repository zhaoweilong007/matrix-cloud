package com.matrix.component;

import cn.dev33.satoken.id.SaIdUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.matrix.constants.SysConstant;
import com.matrix.entity.constans.MatrixConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * 描述：feign拦截器, 为 Feign 的 RCP调用 添加请求头Id-Token
 *
 * @author zwl
 * @since 2022/7/8 15:55
 **/
public class FeignInterceptor implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void apply(RequestTemplate template) {
        template.header(SaIdUtil.ID_TOKEN, SaIdUtil.getToken());
        template.header(SysConstant.APP_NAME, appName);
        if (StpUtil.isLogin()) {
            template.header(MatrixConstants.AUTH_KEY, StpUtil.getTokenValue());
        }
    }
}
