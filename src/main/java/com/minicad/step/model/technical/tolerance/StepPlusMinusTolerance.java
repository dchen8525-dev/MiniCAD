package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved PLUS_MINUS_TOLERANCE.
 * A plus/minus tolerance specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param range tolerance range
 * @param tolerancedMeasure toleranced measure
 */
/**
 * Resolved PLUS_MINUS_TOLERANCE.
 * A plus/minus tolerance specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param range tolerance range
 * @param tolerancedMeasure toleranced measure
 */
public final class StepPlusMinusTolerance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity range;
    private final StepEntity tolerancedMeasure;

    public StepPlusMinusTolerance(int id, String name, StepEntity range, StepEntity tolerancedMeasure) {
        this.id = id;
        this.name = name;
        this.range = range;
        this.tolerancedMeasure = tolerancedMeasure;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRange() {
        return range;
    }

    public StepEntity getTolerancedMeasure() {
        return tolerancedMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlusMinusTolerance that = (StepPlusMinusTolerance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(range, that.range) && Objects.equals(tolerancedMeasure, that.tolerancedMeasure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, range, tolerancedMeasure);
    }

    @Override
    public String toString() {
        return "StepPlusMinusTolerance{" + "id=" + id + "name=" + name + "range=" + range + "tolerancedMeasure=" + tolerancedMeasure + "}";
    }
}
