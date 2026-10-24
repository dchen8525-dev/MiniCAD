package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "position", position, "semiAxisA", semiAxisA,
                "semiAxisB", semiAxisB);
    }
}
