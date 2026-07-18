package com.matrix.idempotent.aspectj;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class RepeatSubmitAspectTest {

    @Test
    void clearsThreadLocalForNonResultReturnValue() throws Exception {
        ThreadLocal<String> keyCache = keyCache();
        keyCache.set("repeat:key");

        new RepeatSubmitAspect().doAfterReturning(null, null, "ok");

        assertNull(keyCache.get());
    }

    @SuppressWarnings("unchecked")
    private static ThreadLocal<String> keyCache() throws Exception {
        Field field = RepeatSubmitAspect.class.getDeclaredField("KEY_CACHE");
        field.setAccessible(true);
        return (ThreadLocal<String>) field.get(null);
    }
}
