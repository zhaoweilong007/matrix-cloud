package com.matrix.common.jackson;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.matrix.common.annotation.AddressDesensitize;
import com.matrix.common.annotation.BankCardDesensitize;
import com.matrix.common.annotation.EmailDesensitize;
import com.matrix.common.annotation.FixedPhoneDesensitize;
import com.matrix.common.annotation.IdCardDesensitize;
import com.matrix.common.annotation.IpDesensitize;
import com.matrix.common.annotation.LicensePlateDesensitize;
import com.matrix.common.annotation.MobileDesensitize;
import com.matrix.common.annotation.NameDesensitize;
import com.matrix.common.annotation.PasswordDesensitize;
import com.matrix.common.annotation.Sensitive;
import com.matrix.common.enums.SensitiveStrategyEnum;
import com.matrix.common.service.ISensitiveService;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;

/**
 * 数据脱敏json序列化工具
 *
 * @author Yjoioooo
 */
@Slf4j
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveStrategyEnum strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            ISensitiveService sensitiveService = SpringUtil.getBean(ISensitiveService.class);
            if (ObjectUtil.isNotNull(sensitiveService) && sensitiveService.isSensitive()) {
                gen.writeString(strategy.desensitizer().apply(value));
            } else {
                gen.writeString(value);
            }
        } catch (BeansException e) {
            log.error("脱敏实现不存在, 采用默认处理 => {}", e.getMessage());
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        // 优先检查 @Sensitive 注解
        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation)
                && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }

        // 检查特定脱敏注解
        if (Objects.equals(String.class, property.getType().getRawClass())) {
            SensitiveStrategyEnum detectedStrategy = detectStrategyFromAnnotation(property);
            if (detectedStrategy != null) {
                this.strategy = detectedStrategy;
                return this;
            }
        }

        return prov.findValueSerializer(property.getType(), property);
    }

    /**
     * 从特定脱敏注解中检测策略
     */
    private SensitiveStrategyEnum detectStrategyFromAnnotation(BeanProperty property) {
        if (property.getAnnotation(MobileDesensitize.class) != null) {
            return SensitiveStrategyEnum.PHONE;
        }
        if (property.getAnnotation(IdCardDesensitize.class) != null) {
            return SensitiveStrategyEnum.ID_CARD;
        }
        if (property.getAnnotation(BankCardDesensitize.class) != null) {
            return SensitiveStrategyEnum.BANK_CARD;
        }
        if (property.getAnnotation(EmailDesensitize.class) != null) {
            return SensitiveStrategyEnum.EMAIL;
        }
        if (property.getAnnotation(NameDesensitize.class) != null) {
            return SensitiveStrategyEnum.CHINESE_NAME;
        }
        if (property.getAnnotation(PasswordDesensitize.class) != null) {
            return SensitiveStrategyEnum.PASSWORD;
        }
        if (property.getAnnotation(IpDesensitize.class) != null) {
            return SensitiveStrategyEnum.IP;
        }
        if (property.getAnnotation(AddressDesensitize.class) != null) {
            return SensitiveStrategyEnum.ADDRESS;
        }
        if (property.getAnnotation(FixedPhoneDesensitize.class) != null) {
            return SensitiveStrategyEnum.FIXED_PHONE;
        }
        if (property.getAnnotation(LicensePlateDesensitize.class) != null) {
            return SensitiveStrategyEnum.LICENSE_PLATE;
        }
        return null;
    }
}
