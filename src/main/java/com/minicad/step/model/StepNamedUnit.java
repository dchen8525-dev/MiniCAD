package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal named unit marker.
 *
 * @param id STEP instance id
 * @param unitKind derived unit kind such as LENGTH_UNIT
 */
public final class StepNamedUnit extends AbstractStepEntity {
    private final String unitKind;

    public StepNamedUnit(int id, String unitKind) {
        super(id, "");
        this.unitKind = unitKind;
    }

    public String getUnitKind() {
        return unitKind;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String unitKind() { return unitKind; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("unitKind", unitKind);
        return state;
    }
}
