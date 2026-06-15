package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved HYPERBOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the hyperbola
 * @param semiAxis1 radius of the major axis
 * @param semiAxis2 radius of the minor axis
 */
/**
 * Resolved HYPERBOLA 2D.
 *
 * @param id step id
 * @param name step label
 * @param position placement of the hyperbola
 * @param semiAxis1 radius of the major axis
 * @param semiAxis2 radius of the minor axis
 */
public final class StepHyperbola2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double semiAxis1;
    private final double semiAxis2;

    public StepHyperbola2D(int id, String name, StepAxis2Placement2D position, double semiAxis1, double semiAxis2) {
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
    public StepAxis2Placement2D position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHyperbola2D that = (StepHyperbola2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "StepHyperbola2D{" + "id=" + id + "name=" + name + "position=" + position + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }
}
