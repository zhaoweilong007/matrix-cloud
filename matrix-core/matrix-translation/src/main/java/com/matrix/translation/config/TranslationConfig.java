package com.matrix.translation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.core.TranslationInterface;
import com.matrix.translation.core.handler.TranslationBeanSerializerModifier;
import com.matrix.translation.core.handler.TranslationHandler;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * 翻译模块配置类
 */
@Slf4j
@AutoConfiguration
public class TranslationConfig {

    /**
     * 所有翻译接口实现列表
     */
    @Autowired
    private List<TranslationInterface<?>> list;

    /**
     * Jackson ObjectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 初始化翻译实现映射，注册到 TranslationHandler 并设置序列化修改器
     */
    @PostConstruct
    public void init() {
        Map<String, TranslationInterface<?>> map = new HashMap<>(list.size());
        for (TranslationInterface<?> trans : list) {
            if (trans.getClass().isAnnotationPresent(TranslationType.class)) {
                TranslationType annotation = trans.getClass().getAnnotation(TranslationType.class);
                map.put(annotation.type(), trans);
            } else {
                log.warn(trans.getClass().getName() + " 翻译实现类未标注 TranslationType 注解!");
            }
        }
        TranslationHandler.TRANSLATION_MAPPER.putAll(map);
        // 设置 Bean 序列化修改器
        objectMapper.setSerializerFactory(
                objectMapper.getSerializerFactory().withSerializerModifier(new TranslationBeanSerializerModifier()));
    }
}
