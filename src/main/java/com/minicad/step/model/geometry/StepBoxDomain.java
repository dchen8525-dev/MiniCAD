package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
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
    private final String name;
    private final StepCartesianPoint corner;
    private final List<Double> dimensions;

    public StepBoxDomain(int id, String name, StepCartesianPoint corner, List<Double> dimensions) {
        this.id = id;
        this.name = name != null ? name : "";
        this.corner = corner;
        this.dimensions = dimensions == null ? null : java.util.List.copyOf(dimensions);
    }

    public StepBoxDomain(int id, StepCartesianPoint corner, List<Double> dimensions) {
        this(id, "", corner, dimensions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepCartesianPoint getCorner() {
        return corner;
    }

    public List<Double> getDimensions() {
        return dimensions;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint corner() { return getCorner(); }
    public List<Double> dimensions() { return getDimensions(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBoxDomain that = (StepBoxDomain) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(corner, that.corner) && Objects.equals(dimensions, that.dimensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, corner, dimensions);
    }

    @Override
    public String toString() {
        return "StepBoxDomain{" + "id=" + id + "name=" + name + "corner=" + corner + "dimensions=" + dimensions + "}";
    }
}
