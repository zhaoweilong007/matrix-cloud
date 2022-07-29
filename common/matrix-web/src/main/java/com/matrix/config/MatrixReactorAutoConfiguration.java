package com.matrix.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/29 11:04
 **/
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class MatrixReactorAutoConfiguration {



}
