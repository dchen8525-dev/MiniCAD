package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DEPRESSION.
 * Represents a depression/pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name depression name
 * @param profile profile definition
 * @param depth depression depth
 * @param direction depression direction
 * @param taperAngle optional taper angle
 */
public final class StepDepression extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepDepression(int id, String name, StepEntity profile, Double depth, StepEntity direction, Double taperAngle) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.taperAngle = taperAngle;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getDepth() {
        return depth;
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
        state.put("depth", depth);
        state.put("direction", direction);
        state.put("taperAngle", taperAngle);
        return state;
    }
}
