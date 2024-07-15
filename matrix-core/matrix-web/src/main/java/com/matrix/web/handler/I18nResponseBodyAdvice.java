package com.matrix.web.handler;

import com.matrix.common.result.R;
import com.matrix.common.util.spring.MessageUtils;
import com.matrix.common.util.string.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对返回消息国际化转换
 *
 * @author ZhaoWeiLong
 * @since 2023/6/25
 **/
@RestControllerAdvice
public class I18nResponseBodyAdvice implements ResponseBodyAdvice<R<?>> {


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        final Class<?> clazz = returnType.getMethod().getReturnType();
        return R.class.isAssignableFrom(clazz);
    }

    @Override
    public R<?> beforeBodyWrite(R<?> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null && StringUtils.isNotEmpty(body.getMessage())) {
            body.setMessage(MessageUtils.message(body.getMessage()));
        }
        return body;
    }

}
