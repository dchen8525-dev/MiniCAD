package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved VALUE_REASON_PAIR.
 * A value-reason pair for classification.
 */
public final class StepValueReasonPair extends AbstractStepEntity {
    private final StepEntity value;
    private final String reason;

    public StepValueReasonPair(int id, String name, StepEntity value, String reason) {
        super(id, name);
        this.value = value;
        this.reason = reason;
    }

    public StepEntity getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("value", value);
        state.put("reason", reason);
        return state;
    }
}
