package com.matrix.web.feature;

import java.lang.annotation.*;

/**
 * 功能开关注解。
 *
 * <p>标注在 Controller 或 Service 方法上，根据 Nacos 配置
 * {@code matrix.feature.toggle.features.<value>} 动态控制方法是否可用。
 * 配置变更后通过 {@code @RefreshScope} 自动生效，无需重启。</p>
 *
 * <p>使用示例：
 * <pre>{@code
 * @FeatureToggle("new-checkout")
 * @PostMapping("/checkout")
 * public R<Void> checkout() { ... }
 * }</pre></p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FeatureToggle {

    /**
     * 功能名称，对应 Nacos 配置 {@code matrix.feature.toggle.features} 中的 key。
     */
    String value();

    /**
     * 功能关闭时的提示消息。
     */
    String message() default "此功能暂未开放";
}
