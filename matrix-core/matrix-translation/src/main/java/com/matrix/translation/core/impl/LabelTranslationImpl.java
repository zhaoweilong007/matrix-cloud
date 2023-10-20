package com.matrix.translation.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.common.result.R;
import com.matrix.common.util.VUtils;
import com.matrix.redis.utils.RedisUtils;
import com.matrix.translation.annotation.Translation;
import com.matrix.translation.annotation.TranslationType;
import com.matrix.translation.api.client.ILabelNameService;
import com.matrix.translation.constant.TransConstant;
import com.matrix.translation.core.TranslationInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 标签翻译实现
 *
 * @author LeonZhou
 */
@RequiredArgsConstructor
@TranslationType(type = TransConstant.LABEL_ID_TO_NAME)
@Slf4j
@Component
public class LabelTranslationImpl implements TranslationInterface<Object> {

    public static final String LABEL_ID_PREFIX = "matrix_label_id:%s#7d";
    public static final String TYPE = "List";
    private final ILabelNameService iLabelNameService;

    @Override
    public Object translation(Object key, Translation translation) {
        String other = translation.other();
        if (Objects.isNull(key)) {
            return null;
        }
        if (!String.class.isAssignableFrom(key.getClass())
                && !List.class.isAssignableFrom(key.getClass())) {
            log.warn("字段类型不支持转换：{}", key.getClass());
            return null;
        }
        String labelName = null;
        if (key instanceof String str) {
            labelName = mapping(str);
        }
        if (key instanceof List<?> list) {
            if (CollUtil.isEmpty(list)) {
                return null;
            }
            Map<String, String> result = new ConcurrentHashMap<>();
            List<String> resultList = new ArrayList<>();
            list.parallelStream().map(o -> (String) o).forEach(s -> {
                if (TYPE.equals(other)) {
                    resultList.add(mapping(s));
                } else {
                    result.put(s, mapping(s));
                }
            });
            if (TYPE.equals(other)) {
                return resultList;
            } else {
                return result;
            }
        }
        return labelName;
    }

    private String mapping(String key) {
        if (key == null) {
            return "";
        }
        final String labelId = String.format(LABEL_ID_PREFIX, key);
        String labelName = RedisUtils.getCacheObject(labelId);
        if (labelName != null) {
            return labelName;
        }

        R<String> res = iLabelNameService.selectNameByLabelId(Long.valueOf(key));
        if (res != null && VUtils.checkRes(res)) {
            labelName = res.getData();
        }
        if (StrUtil.isNotBlank(labelName)) {
            RedisUtils.setCacheObject(labelId, labelName);
        }
        return labelName;
    }
}
