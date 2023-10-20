package com.matrix.web.thread;

import com.matrix.common.context.LbIsolationContextHolder;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.common.model.login.LoginUser;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 线程上下文包装
 */
public class ContextCopyingDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // 主线程
        RequestAttributes context = RequestContextHolder.currentRequestAttributes();
        Long tenantId = TenantContextHolder.getTenantId();
        LoginUser loginUser = LoginUserContextHolder.getUser();
        String version = LbIsolationContextHolder.getVersion();
        // 子线程
        return () -> {
            try {
                // 将变量重新放入到run线程中
                RequestContextHolder.setRequestAttributes(context);
                TenantContextHolder.setTenantId(tenantId);
                LoginUserContextHolder.setUser(loginUser);
                LbIsolationContextHolder.setVersion(version);
                runnable.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                TenantContextHolder.clear();
                LoginUserContextHolder.clear();
                LbIsolationContextHolder.clear();
            }
        };
    }
}
