package com.matrix.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * JS 精度保护序列化器
 * <p>
 * 超过 JS 安全整数范围 (2^53-1 = 9007199254740991) 的 Long 自动序列化为字符串，
 * 防止前端 Number 类型精度丢失
 * </p>
 * <p>
 * 使用方式：
 * <pre>
 * &#64;JsonSerialize(using = NumberSerializer.class)
 * private Long id;
 * </pre>
 * </p>
 *
 */
public class NumberSerializer extends JsonSerializer<Long> {

    /**
     * JS 最大安全整数 (2^53 - 1)
     */
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;

    /**
     * JS 最小安全整数 (-(2^53 - 1))
     */
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value > MAX_SAFE_INTEGER || value < MIN_SAFE_INTEGER) {
            gen.writeString(String.valueOf(value));
        } else {
            gen.writeNumber(value);
        }
    }
}
