package com.matrix.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/11/10 17:25
 **/
@Service
@Slf4j
public class TestStreamConsumer {

    @Bean
    Consumer<DemoMessage> demo() {
        return msg -> {
            log.info("receive message : {}", msg);
        };
    }


}
