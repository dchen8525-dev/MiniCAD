package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATE_TIME_EFFECTIVITY.
 */
public final class StepDateTimeEffectivity extends AbstractStepEntity {
    private final StepEntity effectiveDateTime;

    public StepDateTimeEffectivity(int id, String name, StepEntity effectiveDateTime) {
        super(id, name);
        this.effectiveDateTime = effectiveDateTime;
    }

    public StepEntity getEffectiveDateTime() {
        return effectiveDateTime;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("effectiveDateTime", effectiveDateTime);
        return state;
    }
}
