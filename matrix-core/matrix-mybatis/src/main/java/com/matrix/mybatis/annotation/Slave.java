package com.matrix.mybatis.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * 从库数据源注解。
 *
 * <p>等价于 {@code @DS("slave")}，用于显式指定使用从库数据源。
 * 标注在 Mapper 或 Service 类/方法上即可切换数据源。</p>
 *
 * @author ZhaoWeiLong
 * @since 2026/7/2
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DS("slave")
public @interface Slave {
}
