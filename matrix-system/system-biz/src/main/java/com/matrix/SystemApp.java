package com.matrix;

import com.matrix.annotation.EnableMatrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述：
 *
 * @author zwl
 * @since ${DATE} ${TIME}
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeign
public class SystemApp {
    public static void main(String[] args) {
        SpringApplication.run(SystemApp.class, args);
    }
}