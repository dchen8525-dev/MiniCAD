package com.minicad.step.model.technical.kinematic;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved JOINT_VALUE.
 * Joint value for kinematic joints.
 */
/**
 * Resolved JOINT_VALUE.
 * Joint value for kinematic joints.
 */
public final class StepJointValue implements StepEntity {
    private final int id;
    private final String name;
    private final double value;
    private final String unit;

    public StepJointValue(int id, String name, double value, String unit) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJointValue that = (StepJointValue) o;
        return id == that.id && Objects.equals(name, that.name) && value == that.value && Objects.equals(unit, that.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, unit);
    }

    @Override
    public String toString() {
        return "StepJointValue{" + "id=" + id + "name=" + name + "value=" + value + "unit=" + unit + "}";
    }
}
