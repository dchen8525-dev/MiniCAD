package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOLERANCE_ZONE.
 * Defines a tolerance zone with specific form and appearance.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form tolerance zone form reference
 */
public final class StepToleranceZone extends AbstractStepEntity {
    private final StepEntity form;

    public StepToleranceZone(int id, String name, StepEntity form) {
        super(id, name);
        this.form = form;
    }

    public StepEntity getForm() {
        return form;
    }

    // Record-style accessor
    public StepEntity form() {
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
