package com.matrix.web.xss;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/** 清理 JSON 请求体中的字符串字段。 */
final class XssStringDeserializer extends ValueDeserializer<String> {
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws JacksonException {
        if (!parser.hasToken(JsonToken.VALUE_STRING)) {
            return parser.getValueAsString();
        }
        String value = parser.getString();
        return XssCleanContext.isSkipped() ? value : XssCleaner.clean(value);
    }
}
