package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CENTERED_CIRCLE_PROFILE_DEF.
 * A circular profile with explicit center offset.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius circle radius
 * @param centerOffset center offset distance
 */
public final class StepCenteredCircleProfileDef extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double radius;
    private final double centerOffset;

    public StepCenteredCircleProfileDef(int id, String name, StepAxis2Placement2D position, double radius, double centerOffset) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.centerOffset = centerOffset;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getCenterOffset() {
        return centerOffset;
    }

    // Record-style accessors
    public double radius() { return radius; }
    public double centerOffset() { return centerOffset; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("centerOffset", centerOffset);
        return state;
    }
}
