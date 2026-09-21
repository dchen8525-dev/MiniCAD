package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATUM.
 * A datum reference used in GD&T.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param target referenced target
 * @param orientation orientation flag
 */
public final class StepDatum extends AbstractStepEntity {
    private final String description;
    private final StepEntity target;
    private final boolean orientation;

    public StepDatum(int id, String name, String description, StepEntity target, boolean orientation) {
        super(id, name);
        this.description = description;
        this.target = target;
        this.orientation = orientation;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getTarget() {
        return target;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessor
    public StepEntity target() {
        return target;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("target", target);
        state.put("orientation", orientation);
        return state;
    }
}
