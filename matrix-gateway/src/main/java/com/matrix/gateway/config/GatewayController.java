package com.matrix.gateway.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于健康检查接口
 *
 * @author ZhaoWeiLong
 * @since 2023/9/26
 **/
@RestController
public class GatewayController {

    @RequestMapping(value = "/", method = {RequestMethod.HEAD, RequestMethod.GET})
    public void response() {
    }
}
