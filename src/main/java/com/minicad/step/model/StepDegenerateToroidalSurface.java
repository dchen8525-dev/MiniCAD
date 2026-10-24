package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal DEGENERATE_TOROIDAL_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position surface placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 * @param selectOuter selected torus side flag
 */
public final class StepDegenerateToroidalSurface extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final boolean selectOuter;

    public StepDegenerateToroidalSurface(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, boolean selectOuter) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.selectOuter = selectOuter;
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

    public boolean isSelectOuter() {
        return selectOuter;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }
    public boolean selectOuter() { return isSelectOuter(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "position", position, "majorRadius", majorRadius,
                "minorRadius", minorRadius, "selectOuter", selectOuter);
    }
}
