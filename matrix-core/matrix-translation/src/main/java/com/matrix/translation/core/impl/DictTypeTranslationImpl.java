package com.matrix.translation.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import com.matrix.common.service.IDictService;
import com.matrix.common.util.string.StringUtils;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 字典翻译实现
 */
@RequiredArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
@Slf4j
@Component
public class DictTypeTranslationImpl implements TranslationInterface<Object> {

    private final IDictService dictService;

    @Override
    public Object translation(Object key, Translation translation) {
        String other = translation.other();
        if (Objects.isNull(key)) {
            return null;
        }
        if (!String.class.isAssignableFrom(key.getClass())
                && !Integer.class.isAssignableFrom(key.getClass())
                && !List.class.isAssignableFrom(key.getClass())) {
            log.warn("字段类型不支持转换：{}", key.getClass());
            return null;
        }
        String dictLabel = null;
        if (key instanceof String str) {
            dictLabel = mapping(str, other);
        }
        if (key instanceof Integer str) {
            dictLabel = mapping(String.valueOf(str), other);
        }
        if (key instanceof List<?> list) {
            if (CollUtil.isEmpty(list)) {
                return null;
            }
            Map<String, String> result = new ConcurrentHashMap<>();
            String finalOther = other;
            list.parallelStream().map(o -> (String) o).forEach(s ->
                    result.put(s, mapping(s, finalOther))
            );
            return result;
        }
        return dictLabel;
    }

    private String mapping(String key, String other) {
        if (key instanceof String && StringUtils.isNotBlank(other)) {
            if (Validator.isChinese(key.toString())) {
                return key.toString();
            }
            return dictService.getDictLabel(other, key.toString());
        }
        return key.toString();
    }
}
