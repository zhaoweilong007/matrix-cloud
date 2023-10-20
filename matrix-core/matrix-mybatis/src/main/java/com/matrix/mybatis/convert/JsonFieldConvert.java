package com.matrix.mybatis.convert;

import cn.hutool.core.util.ReflectUtil;
import cn.zhxu.bs.FieldConvertor;
import cn.zhxu.bs.FieldMeta;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.google.common.collect.HashBasedTable;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Method;

/**
 * 为了兼容mybatis plus的注解
 *
 * @author ZhaoWeiLong
 * @since 2023/5/12
 **/
public class JsonFieldConvert implements FieldConvertor.BFieldConvertor {

    private final HashBasedTable<Class, String, AbstractJsonTypeHandler> hashBasedTable = HashBasedTable.create();


    @Override
    public boolean supports(FieldMeta fieldMeta, Class<?> valueType) {
        if (String.class != valueType) {
            return false;
        }
        final TableField annotation = fieldMeta.getField().getAnnotation(TableField.class);
        return (annotation != null && annotation.typeHandler() != null && AbstractJsonTypeHandler.class.isAssignableFrom(annotation.typeHandler()));
    }


    private synchronized AbstractJsonTypeHandler getTypeHandler(FieldMeta fieldMeta) {
        AbstractJsonTypeHandler typeHandler = hashBasedTable.get(fieldMeta.getBeanMeta().getBeanClass(), fieldMeta.getField().getName());
        if (typeHandler == null) {
            final TableField annotation = fieldMeta.getField().getAnnotation(TableField.class);
            final Class<? extends TypeHandler> AbstractJsonTypeHandler = annotation.typeHandler();
            typeHandler = (AbstractJsonTypeHandler) ReflectUtil.newInstance(AbstractJsonTypeHandler, fieldMeta.getField().getType());
            hashBasedTable.put(fieldMeta.getBeanMeta().getBeanClass(), fieldMeta.getField().getName(), typeHandler);
        }
        return typeHandler;
    }

    @Override
    public Object convert(FieldMeta fieldMeta, Object value) {
        final AbstractJsonTypeHandler typeHandler = getTypeHandler(fieldMeta);
        final Method parse = ReflectUtil.getMethodByName(typeHandler.getClass(), "parse");
        return ReflectUtil.invoke(typeHandler, parse, value);
    }
}
