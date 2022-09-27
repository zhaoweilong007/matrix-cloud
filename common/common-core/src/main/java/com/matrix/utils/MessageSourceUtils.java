package com.matrix.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;

/**
 * 描述：国际化消息工具类
 *
 * @author zwl
 * @since 2022/9/27 15:04
 **/
public class MessageSourceUtils {
    public static String getMessage(String code, Object[] arga) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        Assert.notNull(messageSource, "MessageSource cannot be null");
        return messageSource.getMessage(code, arga, LocaleContextHolder.getLocale());
    }

    public static String getMessage(String code) {
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        Assert.notNull(messageSource, "MessageSource cannot be null");
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
