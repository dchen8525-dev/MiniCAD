package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal global unit assigned context.
 *
 * @param id STEP instance id
 * @param units referenced unit entities
 */
public final class StepGlobalUnitAssignedContext extends AbstractStepEntity {
    private final List<StepEntity> units;

    public StepGlobalUnitAssignedContext(int id, List<StepEntity> units) {
        super(id, "");
        this.units = units == null ? null : java.util.List.copyOf(units);
    }

    public List<StepEntity> getUnits() {
        return units;
    }

    // Record-style accessor
    public List<StepEntity> units() {
        return units;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("units", units);
        return state;
    }
}
