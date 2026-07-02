package com.matrix.web.feature;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能开关配置属性。
 *
 * <p>通过 Nacos 动态配置 {@code matrix.feature.toggle.features} 控制各功能启用状态，
 * {@code @RefreshScope} 确保配置变更后实时生效。</p>
 *
 * <p>配置示例：
 * <pre>{@code
 * matrix:
 *   feature:
 *     toggle:
 *       features:
 *         new-checkout: false
 *         export-v2: true
 * }</pre></p>
 *
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "matrix.feature.toggle")
public class FeatureToggleProperties {

    /**
     * 功能启用状态的映射：功能名称 → true/false。
     * 未配置的功能默认返回 {@code false}。
     */
    private Map<String, Boolean> features = new HashMap<>();

    /**
     * 检查指定功能是否启用。
     *
     * @param featureName 功能名称
     * @return true 表示功能已启用
     */
    public boolean isEnabled(String featureName) {
        return Boolean.TRUE.equals(features.get(featureName));
    }
}
