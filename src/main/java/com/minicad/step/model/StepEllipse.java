package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ELLIPSE.
 *
 * @param id step id
 * @param name step label
 * @param position ellipse placement
 * @param semiAxis1 local X semi-axis
 * @param semiAxis2 local Y semi-axis
 */
/**
 * Resolved ELLIPSE.
 *
 * @param id step id
 * @param name step label
 * @param position ellipse placement
 * @param semiAxis1 local X semi-axis
 * @param semiAxis2 local Y semi-axis
 */
public final class StepEllipse implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final double semiAxis1;
    private final double semiAxis2;

    public StepEllipse(int id, String name, StepEntity position, double semiAxis1, double semiAxis2) {
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

    public StepEntity getPosition() {
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
    public StepEntity position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEllipse that = (StepEllipse) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && semiAxis1 == that.semiAxis1 && semiAxis2 == that.semiAxis2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, semiAxis1, semiAxis2);
    }

    @Override
    public String toString() {
        return "StepEllipse{" + "id=" + id + "name=" + name + "position=" + position + "semiAxis1=" + semiAxis1 + "semiAxis2=" + semiAxis2 + "}";
    }
}
