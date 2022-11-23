package com.matrix;

import cn.xuyanwu.spring.file.storage.EnableFileStorage;
import com.matrix.annotation.EnableMatrix;
import org.springframework.boot.SpringApplication;

/**
 * 描述：负责oss、sms、email等服务
 *
 * @author zwl
 * @since 2022/11/23 16:12
 **/
@EnableMatrix
@EnableFileStorage
public class ResourceApp {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApp.class, args);
    }
}
