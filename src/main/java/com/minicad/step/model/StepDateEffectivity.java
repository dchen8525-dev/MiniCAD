package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATE_EFFECTIVITY.
 */
public final class StepDateEffectivity extends AbstractStepEntity {
    private final StepEntity effectiveDate;

    public StepDateEffectivity(int id, String name, StepEntity effectiveDate) {
        super(id, name);
        this.effectiveDate = effectiveDate;
    }

    public StepEntity getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("effectiveDate", effectiveDate);
        return state;
    }
}
