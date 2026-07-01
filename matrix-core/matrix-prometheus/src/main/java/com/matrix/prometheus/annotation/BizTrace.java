package com.matrix.prometheus.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务追踪注解，在 SkyWalking 链路中标记业务 Span 信息。
 *
 * <p>使用后需配置 SkyWalking OAP Server 的 {@code SW_SEARCHABLE_TAG_KEYS}，
 * 增加 {@code biz.type} 和 {@code biz.id} 两个值，重启 OAP 后生效。</p>
 *
 * <pre>{@code
 * @BizTrace(id = "#order.id", type = "'ORDER'")
 * public OrderDTO createOrder(OrderCreateReq req) { ... }
 * }</pre>
 *
 * @author matrix
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BizTrace {

    /** 业务 ID tag 名 */
    String ID_TAG = "biz.id";
    /** 业务类型 tag 名 */
    String TYPE_TAG = "biz.type";

    /** 操作名称（默认使用方法全限定名） */
    String operationName() default "";

    /** 业务编号（支持 SpEL 表达式，如 {@code #order.id}） */
    String id();

    /** 业务类型（支持 SpEL 表达式，如 {@code 'ORDER'}） */
    String type();
}
