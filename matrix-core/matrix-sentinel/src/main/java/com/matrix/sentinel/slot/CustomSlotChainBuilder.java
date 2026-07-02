package com.matrix.sentinel.slot;

import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.DefaultSlotChainBuilder;
import com.matrix.sentinel.slot.degrade.DegradeEarlyWarningSlot;
import com.matrix.sentinel.slot.flow.FlowEarlyWarningSlot;

/**
 * 自定义 Sentinel Slot 链构建器，在默认链末尾追加流控预警和熔断预警 Slot
 */
public class CustomSlotChainBuilder implements SlotChainBuilder {

    /**
     * 构建 Slot 链，在默认链末尾追加预警 Slot
     *
     * @return ProcessorSlotChain
     */
    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultSlotChainBuilder().build();
        chain.addLast(new FlowEarlyWarningSlot());
        chain.addLast(new DegradeEarlyWarningSlot());
        return chain;
    }
}
