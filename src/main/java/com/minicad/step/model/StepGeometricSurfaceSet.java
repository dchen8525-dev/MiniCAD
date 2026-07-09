package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal geometric surface set for surface collections.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric surface elements
 */
/**
 * Minimal geometric surface set for surface collections.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric surface elements
 */
public final class StepGeometricSurfaceSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> elements;

    public StepGeometricSurfaceSet(int id, String name, List<StepEntity> elements) {
        this.id = id;
        this.name = name;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    // Record-style accessor
    public List<StepEntity> elements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricSurfaceSet that = (StepGeometricSurfaceSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, elements);
    }

    @Override
    public String toString() {
        return "StepGeometricSurfaceSet{" + "id=" + id + "name=" + name + "elements=" + elements + "}";
    }
}
