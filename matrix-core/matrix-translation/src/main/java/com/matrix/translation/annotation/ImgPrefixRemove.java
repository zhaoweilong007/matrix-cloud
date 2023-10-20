package com.matrix.translation.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.matrix.translation.config.ImgPrefixRemoveDeSerializer;

import java.lang.annotation.*;

/**
 * 移除图片前缀序列化
 *
 * @author ZhaoWeiLong
 * @since 2023/8/16
 **/
@Inherited
@JacksonAnnotationsInside
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JsonDeserialize(using = ImgPrefixRemoveDeSerializer.class)
public @interface ImgPrefixRemove {
}
