package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved CURVE_2D.
 * A 2D curve defined by a placement and parametric equation.
 *
 * @param id step id
 * @param name step label
 * @param position the 2D placement
 * @param equation the parametric equation coefficients
 */
/**
 * Resolved CURVE_2D.
 * A 2D curve defined by a placement and parametric equation.
 *
 * @param id step id
 * @param name step label
 * @param position the 2D placement
 * @param equation the parametric equation coefficients
 */
public final class StepCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double[] equation;

    public StepCurve2D(int id, String name, StepAxis2Placement2D position, double[] equation) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.equation = equation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double[] getEquation() {
        return equation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCurve2D that = (StepCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(equation, that.equation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, equation);
    }

    @Override
    public String toString() {
        return "StepCurve2D{" + "id=" + id + "name=" + name + "position=" + position + "equation=" + equation + "}";
    }
}
