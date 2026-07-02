package com.matrix.translation.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.matrix.auto.properties.OssProperties;
import com.matrix.common.util.spring.SpringUtils;
import java.io.IOException;

/**
 * 图片前缀移除反序列化器，反序列化时自动去除 OSS 图片前缀
 */
public class ImgPrefixRemoveDeSerializer extends JsonDeserializer<String> {

    /**
     * OSS 配置属性
     */
    private final OssProperties ossProperties = SpringUtils.getBean(OssProperties.class);

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        final String text = jsonParser.getText();
        return StrUtil.removePrefix(text, ossProperties.getPrefix());
    }
}
