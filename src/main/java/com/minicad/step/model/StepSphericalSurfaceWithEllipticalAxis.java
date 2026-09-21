package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SPHERICAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A spherical surface with an elliptical axis definition.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param radius sphere radius
 * @param ellipticalRatio ratio defining the elliptical shape
 */
public final class StepSphericalSurfaceWithEllipticalAxis extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double radius;
    private final double ellipticalRatio;

    public StepSphericalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double radius, double ellipticalRatio) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.ellipticalRatio = ellipticalRatio;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getEllipticalRatio() {
        return ellipticalRatio;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("ellipticalRatio", ellipticalRatio);
        return state;
    }
}
