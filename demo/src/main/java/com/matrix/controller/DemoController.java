package com.matrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/4 11:16
 **/
@RestController
public class DemoController {


    @GetMapping("/hello")
    public String index() {
        return "Hello World!";
    }

}
