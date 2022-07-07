package com.matrix.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/4 11:16
 **/
@RestController
@Api(value = "demoController", tags = "demo测试")
public class DemoController {


    @GetMapping("/hello")
    @ApiOperation(value = "hello", notes = "hello")
    public String index() {
        return "Hello World!";
    }

}
