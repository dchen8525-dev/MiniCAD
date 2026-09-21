package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CENTRE_LINE_ARC_PROFILE_DEF.
 * An arc profile defined along its centre line.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param radius arc radius
 * @param angle sweep angle
 */
public final class StepCentreLineArcProfileDef extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double radius;
    private final double angle;

    public StepCentreLineArcProfileDef(int id, String name, StepAxis2Placement2D position, double radius, double angle) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.angle = angle;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getAngle() {
        return angle;
    }

    // Record-style accessors
    public double radius() { return radius; }
    public double angle() { return angle; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("angle", angle);
        return state;
    }
}
