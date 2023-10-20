package com.matrix.sensitive.model;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.matrix.common.annotation.SensitiveCheck;
import com.matrix.common.enums.SensitiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoWeiLong
 * @since 2023/4/20
 **/
@Data
public class SensitiveCheckDto {

    private Long relateId;

    /**
     * 一次检测全局id
     */
    private Long globalId;

    private String className;

    private Map<String, String> texts = Maps.newHashMap();

    private Map<String, List<String>> imgs = Maps.newHashMap();


    /**
     * 构建检测对象
     *
     * @param relateId 关联id
     * @param object   检测对象
     * @return {@link SensitiveCheckDto}
     */
    public static SensitiveCheckDto build(Long relateId, Object object) {
        final SensitiveCheckDto checkDto = new SensitiveCheckDto();
        handlerField(checkDto, object, null);
        checkDto.setRelateId(relateId);
        checkDto.setClassName(object.getClass().getSimpleName());
        checkDto.setGlobalId(IdUtil.getSnowflakeNextId());
        return checkDto;
    }

    private static void handlerField(SensitiveCheckDto checkDto, Object object, String parentName) {
        final Field[] fields = ReflectUtil.getFields(object.getClass());
        for (Field field : fields) {
            final SensitiveCheck sensitiveCheck = field.getAnnotation(SensitiveCheck.class);
            final Object fieldValue = ReflectUtil.getFieldValue(object, field);
            if (sensitiveCheck != null && fieldValue != null && StrUtil.isNotBlank(fieldValue.toString())) {
                String fieldName = field.getName();
                if (parentName == null) {
                    final Schema property = field.getAnnotation(Schema.class);
                    if (property != null) {
                        fieldName = property.description();
                    }
                } else {
                    fieldName = parentName;
                }
                final SensitiveEnum sensitiveEnum = sensitiveCheck.type();
                switch (sensitiveEnum) {
                    case TEXT:
                        checkDto.getTexts().put(fieldName, fieldValue.toString());
                        break;
                    case IMG:
                        if (checkDto.getImgs().containsKey(fieldName)) {
                            final List<String> list = checkDto.getImgs().get(fieldName);
                            if (fieldValue instanceof String) {
                                list.add(fieldValue.toString());
                            } else if (fieldValue instanceof List) {
                                handlerObject(checkDto, list, field, fieldValue);
                            }
                        } else {
                            final ArrayList<String> list = Lists.newArrayList();
                            checkDto.getImgs().put(fieldName, list);
                            if (fieldValue instanceof String) {
                                list.add(fieldValue.toString());
                            } else if (fieldValue instanceof List) {
                                handlerObject(checkDto, list, field, fieldValue);
                            }
                        }
                        break;
                }
            }
        }
    }

    private static void handlerObject(SensitiveCheckDto checkDto, List<String> list, Field field, Object fieldValue) {
        try {
            final Class<?> aClass = Class.forName(TypeUtil.getTypeArgument(TypeUtil.getType(field)).getTypeName());
            if (aClass.isAssignableFrom(String.class)) {
                list.addAll((Collection<? extends String>) fieldValue);
            } else {
                String fieldName;
                final Schema property = field.getAnnotation(Schema.class);
                if (property != null) {
                    fieldName = property.description();
                } else {
                    fieldName = field.getName();
                }
                ((Collection) fieldValue).forEach(o -> handlerField(checkDto, o, fieldName));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
