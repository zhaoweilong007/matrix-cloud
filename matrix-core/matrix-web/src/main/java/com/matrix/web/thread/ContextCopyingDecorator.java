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
    /**
     * 包装任务，复制主线程请求属性和上下文（租户、用户、灰度版本等）到异步线程
     */
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
