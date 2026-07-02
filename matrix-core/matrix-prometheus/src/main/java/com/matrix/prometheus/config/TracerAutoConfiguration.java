package com.matrix.prometheus.config;

import com.matrix.common.constant.WebFilterOrderConstants;
import com.matrix.prometheus.aspect.BizTraceAspect;
import com.matrix.prometheus.filter.TraceFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 链路追踪自动配置。
 *
 * <p>自动注册 TraceFilter 过滤器（响应中返回 traceId）和
 * BizTraceAspect 切面（SkyWalking Span 业务标签标记）。</p>
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matrix.monitor", value = "traceEnable", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TracerAutoConfiguration {

    /**
     * 创建 TraceFilter 过滤器，响应 header 设置 traceId
     */
    @Bean
    public FilterRegistrationBean<TraceFilter> traceFilter() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.setOrder(WebFilterOrderConstants.TRACE_FILTER);
        return registrationBean;
    }

    /**
     * 创建 BizTraceAspect 切面，在业务方法上标记 SkyWalking Span 业务标签。
     */
    @Bean
    @ConditionalOnClass(name = "org.apache.skywalking.apm.toolkit.trace.ActiveSpan")
    public BizTraceAspect bizTraceAspect() {
        return new BizTraceAspect();
    }
}
