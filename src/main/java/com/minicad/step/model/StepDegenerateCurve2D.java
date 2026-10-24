package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DEGENERATE_CURVE_2D.
 *
 * @param id step id
 * @param name step label
 * @param point the degenerate point
 */
public final class StepDegenerateCurve2D extends AbstractStepEntity {
    private final StepCartesianPoint point;

    public StepDegenerateCurve2D(int id, String name, StepCartesianPoint point) {
        super(id, name);
        this.point = point;
    }

    public StepCartesianPoint getPoint() {
        return point;
    }

    // Record-style accessors
    public StepCartesianPoint point() { return getPoint(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "point", point);
    }
}
