package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
 * A toroidal surface where the major and minor axes are optionally defined by explicit curves.
 * For B-Rep generation, this is treated as a standard toroidal surface using the radius parameters.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param majorAxisCurve optional curve defining the major axis path
 * @param minorAxisCurve optional curve defining the minor axis profile
 */
public final class StepToroidalSurfaceWithSpecifiedBends extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final StepEntity majorAxisCurve;
    private final StepEntity minorAxisCurve;

    public StepToroidalSurfaceWithSpecifiedBends(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, StepEntity majorAxisCurve, StepEntity minorAxisCurve) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.majorAxisCurve = majorAxisCurve;
        this.minorAxisCurve = minorAxisCurve;
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

    public StepEntity getMajorAxisCurve() {
        return majorAxisCurve;
    }

    public StepEntity getMinorAxisCurve() {
        return minorAxisCurve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }
    public StepEntity majorAxisCurve() { return getMajorAxisCurve(); }
    public StepEntity minorAxisCurve() { return getMinorAxisCurve(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("majorRadius", majorRadius);
        state.put("minorRadius", minorRadius);
        state.put("majorAxisCurve", majorAxisCurve);
        state.put("minorAxisCurve", minorAxisCurve);
        return state;
    }
}
