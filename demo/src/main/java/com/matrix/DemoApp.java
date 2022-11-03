package com.matrix;

import com.matrix.annotation.EnableMatrix;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/3 14:26
 **/
@SpringBootApplication
@EnableMatrix
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }
}
