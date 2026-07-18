package com.matrix.feign.chooser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.ServiceInstance;

class RoundRuleChooserTest {

    @Test
    void returnsNullForEmptyInstances() {
        assertNull(new RoundRuleChooser().choose(List.of()));
    }

    @Test
    void handlesCounterOverflowWithoutNegativeIndex() throws Exception {
        RoundRuleChooser chooser = new RoundRuleChooser();
        Field position = RoundRuleChooser.class.getDeclaredField("position");
        position.setAccessible(true);
        ((AtomicInteger) position.get(chooser)).set(Integer.MIN_VALUE);
        ServiceInstance first = mock(ServiceInstance.class);
        ServiceInstance second = mock(ServiceInstance.class);

        ServiceInstance selected = assertDoesNotThrow(() -> chooser.choose(List.of(first, second)));

        assertSame(first, selected);
    }
}
