package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOROIDAL_SURFACE_WITH_ELLIPTICAL_AXIS.
 * A toroidal surface where the axis is defined by an elliptical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param ellipticalRatio ratio defining the elliptical cross-section
 */
public final class StepToroidalSurfaceWithEllipticalAxis extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final double ellipticalRatio;

    public StepToroidalSurfaceWithEllipticalAxis(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, double ellipticalRatio) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.ellipticalRatio = ellipticalRatio;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    public double getEllipticalRatio() {
        return ellipticalRatio;
    }

    // Record-style accessors
    public StepAxis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("majorRadius", majorRadius);
        state.put("minorRadius", minorRadius);
        state.put("ellipticalRatio", ellipticalRatio);
        return state;
    }
}
