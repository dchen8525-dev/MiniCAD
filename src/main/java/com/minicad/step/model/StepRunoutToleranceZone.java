package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RUNOUT_TOLERANCE_ZONE.
 * A tolerance zone specifically for runout tolerances.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form zone form reference
 */
public final class StepRunoutToleranceZone extends AbstractStepEntity {
    private final StepEntity form;

    public StepRunoutToleranceZone(int id, String name, StepEntity form) {
        super(id, name);
        this.form = form;
    }

    public StepEntity getForm() {
        return form;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("form", form);
        return state;
    }
}
