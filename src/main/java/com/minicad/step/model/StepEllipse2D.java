package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ELLIPSE_2D.
 * An ellipse in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param semiAxis1 semi-major axis length
 * @param semiAxis2 semi-minor axis length
 */
/**
 * Resolved ELLIPSE_2D.
 * An ellipse in 2D parameter space.
 *
 * @param id step id
 * @param name step label
 * @param position 2D placement (center and direction)
 * @param semiAxis1 semi-major axis length
 * @param semiAxis2 semi-minor axis length
 */
public final class StepEllipse2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double semiAxis1;
    private final double semiAxis2;

    public StepEllipse2D(int id, String name, StepAxis2Placement2D position, double semiAxis1, double semiAxis2) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
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

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement2D position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEllipse2D that = (StepEllipse2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "StepEllipse2D{" + "id=" + id + "name=" + name + "position=" + position + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }
}
