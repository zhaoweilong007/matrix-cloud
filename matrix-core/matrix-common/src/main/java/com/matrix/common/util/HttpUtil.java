package com.matrix.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson2.JSONObject;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * http请求工具类,用于云帐户
 */
@Slf4j
public class HttpUtil {
    public static JSONObject executePost(String url, String parameters) throws IOException {
        if (StrUtil.isBlank(parameters)) {
            return null;
        }

        final HttpResponse response = HttpRequest.post(url)
                .contentType(ContentType.JSON.getValue())
                .body(parameters).execute();

        if (response.getStatus() != HttpStatus.HTTP_OK) {
            log.error("api 请求错误：{}", response);
            throw new ServiceException(SystemErrorTypeEnum.SYSTEM_ERROR);
        }
        final String body = response.body();
        log.debug("result body:{}", body);
        return JSONObject.parseObject(body);
    }

}