package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BUY_FROM_USAGE_OPTION.
 */
public final class StepBuyFromUsageOption extends AbstractStepEntity {
    private final StepEntity supplier;

    public StepBuyFromUsageOption(int id, String name, StepEntity supplier) {
        super(id, name);
        this.supplier = supplier;
    }

    public StepEntity getSupplier() {
        return supplier;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("supplier", supplier);
        return state;
    }
}
