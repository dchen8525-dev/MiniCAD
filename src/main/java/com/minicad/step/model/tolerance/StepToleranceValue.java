package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TOLERANCE_VALUE.
 * A tolerance value specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param lowerBound lower bound value
 * @param upperBound upper bound value
 */
/**
 * Resolved TOLERANCE_VALUE.
 * A tolerance value specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param lowerBound lower bound value
 * @param upperBound upper bound value
 */
public final class StepToleranceValue implements StepEntity {
    private final int id;
    private final String name;
    private final double lowerBound;
    private final double upperBound;

    public StepToleranceValue(int id, String name, double lowerBound, double upperBound) {
        this.id = id;
        this.name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        StepToleranceValue that = (StepToleranceValue) o;
        return id == that.id && Objects.equals(name, that.name) && lowerBound == that.lowerBound && upperBound == that.upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lowerBound, upperBound);
    }

    @Override
    public String toString() {
        return "StepToleranceValue{" + "id=" + id + "name=" + name + "lowerBound=" + lowerBound + "upperBound=" + upperBound + "}";
    }
}
