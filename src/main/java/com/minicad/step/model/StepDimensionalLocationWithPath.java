package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved DIMENSIONAL_LOCATION_WITH_PATH.
 * A dimensional location that includes a path definition for the measurement route.
 */
/**
 * Resolved DIMENSIONAL_LOCATION_WITH_PATH.
 * A dimensional location that includes a path definition for the measurement route.
 */
public final class StepDimensionalLocationWithPath implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity toleratedShape;
    private final StepEntity path;

    public StepDimensionalLocationWithPath(int id, String name, String description, StepEntity toleratedShape, StepEntity path) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.toleratedShape = toleratedShape;
        this.path = path;
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

    public StepEntity getToleratedShape() {
        return toleratedShape;
    }

    public StepEntity getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDimensionalLocationWithPath that = (StepDimensionalLocationWithPath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(toleratedShape, that.toleratedShape) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, toleratedShape, path);
    }

    @Override
    public String toString() {
        return "StepDimensionalLocationWithPath{" + "id=" + id + "name=" + name + "description=" + description + "toleratedShape=" + toleratedShape + "path=" + path + "}";
    }
}
