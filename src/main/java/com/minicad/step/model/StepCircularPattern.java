package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CIRCULAR_PATTERN.
 * Represents a circular pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param axis pattern axis
 * @param angularSpacing angular spacing between features
 * @param count number of features
 */
/**
 * Resolved CIRCULAR_PATTERN.
 * Represents a circular pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param axis pattern axis
 * @param angularSpacing angular spacing between features
 * @param count number of features
 */
public final class StepCircularPattern implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity baseFeature;
    private final StepEntity axis;
    private final Double angularSpacing;
    private final Integer count;

    public StepCircularPattern(int id, String name, StepEntity baseFeature, StepEntity axis, Double angularSpacing, Integer count) {
        this.id = id;
        this.name = name;
        this.baseFeature = baseFeature;
        this.axis = axis;
        this.angularSpacing = angularSpacing;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBaseFeature() {
        return baseFeature;
    }

    public StepEntity getAxis() {
        return axis;
    }

    public Double getAngularSpacing() {
        return angularSpacing;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCircularPattern that = (StepCircularPattern) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baseFeature, that.baseFeature) && Objects.equals(axis, that.axis) && Objects.equals(angularSpacing, that.angularSpacing) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baseFeature, axis, angularSpacing, count);
    }

    @Override
    public String toString() {
        return "StepCircularPattern{" + "id=" + id + "name=" + name + "baseFeature=" + baseFeature + "axis=" + axis + "angularSpacing=" + angularSpacing + "count=" + count + "}";
    }
}