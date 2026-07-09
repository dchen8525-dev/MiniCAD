package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_SURFACE_SIDE_STYLE.
 *
 * @param id step id
 * @param name predefined surface side style name
 */
/**
 * Minimal PRE_DEFINED_SURFACE_SIDE_STYLE.
 *
 * @param id step id
 * @param name predefined surface side style name
 */
public final class StepPreDefinedSurfaceSideStyle implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedSurfaceSideStyle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPreDefinedSurfaceSideStyle that = (StepPreDefinedSurfaceSideStyle) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedSurfaceSideStyle{" + "id=" + id + "name=" + name + "}";
    }
}
