package com.matrix.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.matrix.common.enums.SensitiveStrategyEnum;
import com.matrix.common.jackson.SensitiveJsonSerializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 地址脱敏注解
 * <p>
 * 示例：北京市海淀区****
 * </p>
 *
 * @author matrix
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface AddressDesensitize {
}
