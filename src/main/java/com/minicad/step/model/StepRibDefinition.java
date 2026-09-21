package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RIB_DEFINITION.
 * A rib definition entity.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 * @param taperAngle optional taper angle
 */
public final class StepRibDefinition extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepRibDefinition(int id, String name, StepEntity profile, Double height, StepEntity direction, Double taperAngle) {
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
