package com.matrix.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class EmailProperties {
    private String from;
}
