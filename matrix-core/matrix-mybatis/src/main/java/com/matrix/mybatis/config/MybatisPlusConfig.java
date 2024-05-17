package com.matrix.mybatis.config;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.entity.BaseEntity;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.util.servlet.ServletUtils;
import io.vavr.control.Option;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 描述：<p>mybatis plus配置</p>
 *
 * @author ZhaoWeiLong
 * @since 2023/2/17
 **/
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@MapperScan(basePackages = "${mybatis-plus.mapperPackage}")
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        final PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        //分页合理化
        paginationInnerInterceptor.setOverflow(false);
        //查询最大数量 防止sql没有限制条件 数据量太大
        paginationInnerInterceptor.setMaxLimit(1000L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }
    /**
     * 使用网卡信息绑定雪花生成器
     * 防止集群雪花ID重复
     */
    @Primary
    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(NetUtil.getLocalhost());
    }


    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        // 自动填充参数类
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                    if (baseEntity.getCreatedBy() == null) {
                        if (ServletUtils.isServlet()) {
                            baseEntity.setCreatedBy(getUserId());
                        }
                    }
                    if (baseEntity.getCreatedAt() == null) {
                        baseEntity.setCreatedAt(LocalDateTime.now());
                    }
                    this.updateFill(metaObject);
                }

            }

            @Override
            public void updateFill(MetaObject metaObject) {
                if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity baseEntity) {
                    if (ServletUtils.isServlet()) {
                        baseEntity.setUpdatedBy(getUserId());
                    }
                    baseEntity.setUpdatedAt(LocalDateTime.now());
                }
            }
        };
    }

    private Long getUserId() {
        return Option.of(LoginUserContextHolder.getUser()).map(LoginUser::getUserId)
                .getOrElse(ServletUtils.getUserIdByRequestHead());
    }

}
