package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOROIDAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position torus placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 */
public final class StepToroidalSurface extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;

    public StepToroidalSurface(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
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
        return state;
    }
}
