package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CONICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A conical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAngle semi-angle of the cone
 * @param semiAxisA first semi-axis of the ellipse at base
 * @param semiAxisB second semi-axis of the ellipse at base
 */
public final class StepConicalSurfaceWithEllipticalAxis extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double semiAngle;
    private final double semiAxisA;
    private final double semiAxisB;

    public StepConicalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double semiAngle, double semiAxisA, double semiAxisB) {
        super(id, name);
        this.position = position;
        this.semiAngle = semiAngle;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getSemiAngle() {
        return semiAngle;
    }

    public double getSemiAxisA() {
        return semiAxisA;
    }

    public double getSemiAxisB() {
        return semiAxisB;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double semiAxisA() { return getSemiAxisA(); }
    public double semiAxisB() { return getSemiAxisB(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("semiAngle", semiAngle);
        state.put("semiAxisA", semiAxisA);
        state.put("semiAxisB", semiAxisB);
        return state;
    }
}
