package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved MOTION_CONSTRAINT.
 * A motion constraint for kinematic joints.
 */
/**
 * Resolved MOTION_CONSTRAINT.
 * A motion constraint for kinematic joints.
 */
public final class StepMotionConstraint implements StepEntity {
    private final int id;
    private final String name;
    private final String constraintType;
    private final double lowerLimit;
    private final double upperLimit;

    public StepMotionConstraint(int id, String name, String constraintType, double lowerLimit, double upperLimit) {
        this.id = id;
        this.name = name;
        this.constraintType = constraintType;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMotionConstraint that = (StepMotionConstraint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(constraintType, that.constraintType) && lowerLimit == that.lowerLimit && upperLimit == that.upperLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, constraintType, lowerLimit, upperLimit);
    }

    @Override
    public String toString() {
        return "StepMotionConstraint{" + "id=" + id + "name=" + name + "constraintType=" + constraintType + "lowerLimit=" + lowerLimit + "upperLimit=" + upperLimit + "}";
    }
}
