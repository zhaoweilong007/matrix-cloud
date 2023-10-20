package com.matrix.translation.config;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.matrix.auto.properties.OssProperties;
import com.matrix.common.util.SpringUtils;

import java.io.IOException;

/**
 * 移除图片前缀
 *
 * @author ZhaoWeiLong
 * @since 2023/8/16
 **/
public class ImgPrefixRemoveDeSerializer extends JsonDeserializer<String> {

    private final OssProperties ossProperties = SpringUtils.getBean(OssProperties.class);

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String text = jsonParser.getText();
        return StrUtil.removePrefix(text, ossProperties.getPrefix());
    }
}
