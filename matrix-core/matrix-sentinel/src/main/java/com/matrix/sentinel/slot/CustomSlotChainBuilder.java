package com.matrix.sentinel.slot;

import com.alibaba.csp.sentinel.slotchain.ProcessorSlotChain;
import com.alibaba.csp.sentinel.slotchain.SlotChainBuilder;
import com.alibaba.csp.sentinel.slots.DefaultSlotChainBuilder;
import com.matrix.sentinel.slot.degrade.DegradeEarlyWarningSlot;
import com.matrix.sentinel.slot.flow.FlowEarlyWarningSlot;

public class CustomSlotChainBuilder implements SlotChainBuilder {


    @Override
    public ProcessorSlotChain build() {
        ProcessorSlotChain chain = new DefaultSlotChainBuilder().build();
        chain.addLast(new FlowEarlyWarningSlot());
        chain.addLast(new DegradeEarlyWarningSlot());
        return chain;
    }
}