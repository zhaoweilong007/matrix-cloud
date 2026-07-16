package com.matrix.web.thread;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.matrix.common.context.TenantContextHolder;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.enums.PlatformUserTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ContextCopyingDecoratorTest {

    @AfterEach
    void clearContext() {
        TenantContextHolder.clear();
        TerminalContextHolder.clear();
    }

    @Test
    void decoratesNonWebTaskAndCleansWorkerContext() {
        TenantContextHolder.setTenantId(42L);
        TerminalContextHolder.setUsertype(PlatformUserTypeEnum.SYS_USER);

        Runnable decorated = new ContextCopyingDecorator().decorate(() -> {
            assertEquals(42L, TenantContextHolder.getTenantId());
            assertEquals(PlatformUserTypeEnum.SYS_USER, TerminalContextHolder.getUserType());
        });
        decorated.run();

        assertNull(TenantContextHolder.getTenantId());
        assertNull(TerminalContextHolder.getUserType());
    }
}
