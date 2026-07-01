package com.matrix.gateway.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.matrix.common.jackson.BigNumberSerializer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson 序列化配置（Gateway WebFlux）。
 *
 * @author ZhaoWeiLong
 * @since 2023/6/15
 **/
@Slf4j
@Configuration
public class JacksonConfig {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";

    @Bean
    public Module javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Long.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(Long.TYPE, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigInteger.class, BigNumberSerializer.INSTANCE);
        javaTimeModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        javaTimeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(PATTERN_DATE)));
        javaTimeModule.addSerializer(LocalTime.class,
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(PATTERN_TIME)));
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(PATTERN_DATETIME)));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(PATTERN_DATETIME)));
        javaTimeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(PATTERN_DATE)));
        javaTimeModule.addDeserializer(LocalTime.class,
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(PATTERN_TIME)));
        log.info("gateway jackson config init");
        return javaTimeModule;
    }
}
