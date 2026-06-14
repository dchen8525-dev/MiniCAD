package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;
/**
 * Resolved TESSELLATED_COORDINATE_SET.
 * A set of coordinates for tessellated geometry.
 */
/**
 * Resolved TESSELLATED_COORDINATE_SET.
 * A set of coordinates for tessellated geometry.
 */
public final class StepTessellatedCoordinateSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> coordinates;

    public StepTessellatedCoordinateSet(int id, String name, List<StepEntity> coordinates) {
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

    public List<StepEntity> getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTessellatedCoordinateSet that = (StepTessellatedCoordinateSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates);
    }

    @Override
    public String toString() {
        return "StepTessellatedCoordinateSet{" + "id=" + id + "name=" + name + "coordinates=" + coordinates + "}";
    }
}
