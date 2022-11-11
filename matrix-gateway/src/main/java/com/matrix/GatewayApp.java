package com.matrix;

import com.matrix.annotation.EnableMatrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述：网关服务
 *
 * @author zwl
 * @since ${DATE} ${TIME}
 **/
@SpringBootApplication
@EnableMatrix
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }
}