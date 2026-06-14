package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal BOX_DOMAIN.
 *
 * @param id step id
 * @param corner box corner point
 * @param dimensions box dimensions in STEP order
 */
/**
 * Minimal BOX_DOMAIN.
 *
 * @param id step id
 * @param corner box corner point
 * @param dimensions box dimensions in STEP order
 */
public final class StepBoxDomain implements StepEntity {
    private final int id;
    private final StepCartesianPoint corner;
    private final List<Double> dimensions;

    public StepBoxDomain(int id, StepCartesianPoint corner, List<Double> dimensions) {
        this.id = id;
        this.corner = corner;
        this.dimensions = dimensions == null ? null : java.util.List.copyOf(dimensions);
    }

    public int getId() {
        return id;
    }

    public StepCartesianPoint getCorner() {
        return corner;
    }

    public List<Double> getDimensions() {
        return dimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBoxDomain that = (StepBoxDomain) o;
        return id == that.id && Objects.equals(corner, that.corner) && Objects.equals(dimensions, that.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, corner, dimensions);
    }

    @Override
    public String toString() {
        return "StepBoxDomain{" + "id=" + id + "corner=" + corner + "dimensions=" + dimensions + "}";
    }
}
