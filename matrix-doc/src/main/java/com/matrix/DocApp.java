package com.matrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 描述：api文档聚合
 * 文档地址:<a href="http://ip:port/doc.html">...</a>
 *
 * @author zwl
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class DocApp {
    public static void main(String[] args) {
        SpringApplication.run(DocApp.class, args);
    }
}