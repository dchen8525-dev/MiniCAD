package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CURVE_2D.
 * A 2D curve defined by a placement and parametric equation.
 *
 * @param id step id
 * @param name step label
 * @param position the 2D placement
 * @param equation the parametric equation coefficients
 */
public final class StepCurve2D extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double[] equation;

    public StepCurve2D(int id, String name, StepAxis2Placement2D position, double[] equation) {
        super(id, name);
        this.position = position;
        this.equation = equation;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double[] getEquation() {
        return equation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement2D position() { return getPosition(); }
    public double[] equation() { return getEquation(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "position", position, "equation", equation);
    }
}
