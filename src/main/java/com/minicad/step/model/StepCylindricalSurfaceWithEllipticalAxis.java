package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CYLINDRICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A cylindrical surface with an elliptical cross-section.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param semiAxisA first semi-axis of the ellipse
 * @param semiAxisB second semi-axis of the ellipse
 */
public final class StepCylindricalSurfaceWithEllipticalAxis extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double semiAxisA;
    private final double semiAxisB;

    public StepCylindricalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double semiAxisA, double semiAxisB) {
        super(id, name);
        this.position = position;
        this.semiAxisA = semiAxisA;
        this.semiAxisB = semiAxisB;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
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
        state.put("semiAxisA", semiAxisA);
        state.put("semiAxisB", semiAxisB);
        return state;
    }
}
