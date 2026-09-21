package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROTRUSION.
 * Represents a protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name protrusion name
 * @param profile profile definition
 * @param height protrusion height
 * @param direction protrusion direction
 * @param taperAngle optional taper angle
 */
public final class StepProtrusion extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepProtrusion(int id, String name, StepEntity profile, Double height, StepEntity direction, Double taperAngle) {
        super(id, name);
        this.profile = profile;
        this.height = height;
        this.direction = direction;
        this.taperAngle = taperAngle;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getHeight() {
        return height;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getTaperAngle() {
        return taperAngle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("height", height);
        state.put("direction", direction);
        state.put("taperAngle", taperAngle);
        return state;
    }
}
