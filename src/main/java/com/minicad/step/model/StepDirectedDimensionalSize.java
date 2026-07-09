package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DIRECTED_DIMENSIONAL_SIZE.
 * A dimensional size with a direction for tolerance.
 *
 * @param id STEP instance id
 * @param name size name
 * @param magnitude size magnitude
 * @param direction reference direction for the measurement
 */
/**
 * Resolved DIRECTED_DIMENSIONAL_SIZE.
 * A dimensional size with a direction for tolerance.
 *
 * @param id STEP instance id
 * @param name size name
 * @param magnitude size magnitude
 * @param direction reference direction for the measurement
 */
public final class StepDirectedDimensionalSize implements StepEntity {
    private final int id;
    private final String name;
    private final double magnitude;
    private final StepEntity direction;

    public StepDirectedDimensionalSize(int id, String name, double magnitude, StepEntity direction) {
        this.id = id;
        this.name = name;
        this.magnitude = magnitude;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDirectedDimensionalSize that = (StepDirectedDimensionalSize) o;
        return id == that.id && Objects.equals(name, that.name) && magnitude == that.magnitude && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, magnitude, direction);
    }

    @Override
    public String toString() {
        return "StepDirectedDimensionalSize{" + "id=" + id + "name=" + name + "magnitude=" + magnitude + "direction=" + direction + "}";
    }
}
