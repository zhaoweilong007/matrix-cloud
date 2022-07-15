package com.matrix.config;

import com.matrix.service.UserService;
import com.matrix.service.impl.DefaultUseServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/8 16:22
 **/
@Import(MatrixMybatisAutoConfiguration.class)
public class MatrixAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public UserService userService() {
        return new DefaultUseServiceImpl();
    }

}
