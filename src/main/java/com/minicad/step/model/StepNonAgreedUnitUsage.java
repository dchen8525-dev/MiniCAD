package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * NON_AGREED_UNIT_USAGE entity model.
 * Represents usage of a unit that has not been agreed upon in a standards context.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unit reference to the unit being used
 */
public final class StepNonAgreedUnitUsage extends AbstractStepEntity {
    private final Object unit; // Can be any unit type

    public StepNonAgreedUnitUsage(int id, String name, Object unit) {
        super(id, name);
        this.unit = unit;
    }

    public Object getUnit() {
        return unit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("unit", unit);
        return state;
    }
}
