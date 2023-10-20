package com.matrix.strategy.config;

import com.matrix.strategy.service.BusinessHandler;
import com.matrix.strategy.service.BusinessHandlerChooser;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 策略模式自动注入配置
 */
@AutoConfiguration
public class StrategyConfiguration {

    @Bean
    public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
        BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
        businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
        return businessHandlerChooser;
    }

}
