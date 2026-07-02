package com.matrix.mybatis.convert;

import cn.hutool.core.util.ReflectUtil;
import cn.zhxu.bs.FieldConvertor;
import cn.zhxu.bs.FieldMeta;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.google.common.collect.HashBasedTable;
import java.lang.reflect.Method;
import org.apache.ibatis.type.TypeHandler;

/**
 * 为了兼容mybatis plus的注解
 *
 **/
public class JsonFieldConvert implements FieldConvertor.BFieldConvertor {

    /** 缓存实体类字段对应的 JsonTypeHandler，避免重复创建 */
    private final HashBasedTable<Class, String, AbstractJsonTypeHandler> hashBasedTable = HashBasedTable.create();

    /**
     * 判断字段是否需要 JSON 转换：字段标注了 @TableField 且 typeHandler 为 AbstractJsonTypeHandler 子类。
     */
    @Override
    public boolean supports(FieldMeta fieldMeta, Class<?> valueType) {
        if (String.class != valueType) {
            return false;
        }
        final TableField annotation = fieldMeta.getField().getAnnotation(TableField.class);
        return (annotation != null
                && annotation.typeHandler() != null
                && AbstractJsonTypeHandler.class.isAssignableFrom(annotation.typeHandler()));
    }

    /**
     * 获取或创建字段对应的 TypeHandler 实例。
     */
    private synchronized AbstractJsonTypeHandler getTypeHandler(FieldMeta fieldMeta) {
        AbstractJsonTypeHandler typeHandler = hashBasedTable.get(
                fieldMeta.getBeanMeta().getBeanClass(), fieldMeta.getField().getName());
        if (typeHandler == null) {
            final TableField annotation = fieldMeta.getField().getAnnotation(TableField.class);
            final Class<? extends TypeHandler> AbstractJsonTypeHandler = annotation.typeHandler();
            typeHandler = (AbstractJsonTypeHandler) ReflectUtil.newInstance(
                    AbstractJsonTypeHandler, fieldMeta.getField().getType());
            hashBasedTable.put(
                    fieldMeta.getBeanMeta().getBeanClass(), fieldMeta.getField().getName(), typeHandler);
        }
        return typeHandler;
    }

    /**
     * 执行 JSON 字符串到 Java 对象的转换。
     */
    @Override
    public Object convert(FieldMeta fieldMeta, Object value) {
        final AbstractJsonTypeHandler typeHandler = getTypeHandler(fieldMeta);
        final Method parse = ReflectUtil.getMethodByName(typeHandler.getClass(), "parse");
        return ReflectUtil.invoke(typeHandler, parse, value);
    }
}
