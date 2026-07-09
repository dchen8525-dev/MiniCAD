package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CARTESIAN_POINT.
 *
 * @param id step id
 * @param name step label
 * @param coordinates 2D or 3D coordinates
 */
/**
 * Resolved CARTESIAN_POINT.
 *
 * @param id step id
 * @param name step label
 * @param coordinates 2D or 3D coordinates
 */
public final class StepCartesianPoint implements StepEntity {
    private final int id;
    private final String name;
    private final List<Double> coordinates;

    public StepCartesianPoint(int id, String name, List<Double> coordinates) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates == null ? null : java.util.List.copyOf(coordinates);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    // Record-style accessor
    public List<Double> coordinates() { return getCoordinates(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCartesianPoint that = (StepCartesianPoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates);
    }

    @Override
    public String toString() {
        return "StepCartesianPoint{" + "id=" + id + "name=" + name + "coordinates=" + coordinates + "}";
    }
}
