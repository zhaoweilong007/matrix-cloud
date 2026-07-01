package com.matrix.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.entity.BaseEntity;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.util.servlet.ServletUtils;
import io.vavr.control.Option;
import java.time.LocalDateTime;
import java.util.Objects;
import org.apache.ibatis.reflection.MetaObject;

/**
 * MyBatis-Plus 自动填充处理器。
 *
 * <p>自动填充 BaseEntity 的审计字段（创建人/时间、更新人/时间）。
 * 仅在 Servlet 请求上下文中自动获取当前登录用户。</p>
 *
 * @author matrix
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {

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

    private Long getUserId() {
        return Option.of(LoginUserContextHolder.getUser())
                .map(LoginUser::getUserId)
                .getOrElse(ServletUtils.getUserIdByRequestHead());
    }
}
