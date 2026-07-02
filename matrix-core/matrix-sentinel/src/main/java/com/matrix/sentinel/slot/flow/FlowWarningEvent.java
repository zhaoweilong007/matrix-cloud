package com.matrix.sentinel.slot.flow;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 流控预警事件。
 *
 * <p>当流量达到预警阈值（默认 80%）时发布此事件，
 * 下游监听器可对接邮件/短信/Webhook 等告警通道。</p>
 */
@Getter
public class FlowWarningEvent extends ApplicationEvent {

    /** 资源名称 */
    private final String resource;
    /** 预警阈值（实际配置阈值的百分比） */
    private final double warningCount;
    /** 原始流控阈值 */
    private final double originCount;

    public FlowWarningEvent(Object source, String resource, double warningCount, double originCount) {
        super(source);
        this.resource = resource;
        this.warningCount = warningCount;
        this.originCount = originCount;
    }
}
