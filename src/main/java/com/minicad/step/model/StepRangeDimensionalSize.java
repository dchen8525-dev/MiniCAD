package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RANGE_DIMENSIONAL_SIZE.
 * Dimensional size with range bounds.
 */
/**
 * Resolved RANGE_DIMENSIONAL_SIZE.
 * Dimensional size with range bounds.
 */
public final class StepRangeDimensionalSize implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final double lowerBound;
    private final double upperBound;

    public StepRangeDimensionalSize(int id, String name, String description, double lowerBound, double upperBound) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRangeDimensionalSize that = (StepRangeDimensionalSize) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && lowerBound == that.lowerBound && upperBound == that.upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, lowerBound, upperBound);
    }

    @Override
    public String toString() {
        return "StepRangeDimensionalSize{" + "id=" + id + "name=" + name + "description=" + description + "lowerBound=" + lowerBound + "upperBound=" + upperBound + "}";
    }
}
