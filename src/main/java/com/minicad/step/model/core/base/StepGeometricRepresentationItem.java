package com.minicad.step.model.core.base;

import java.util.Objects;

/**
 * Minimal geometric representation item marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
/**
 * Minimal geometric representation item marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public final class StepGeometricRepresentationItem implements StepEntity {
    private final int id;
    private final String name;

    public StepGeometricRepresentationItem(int id, String name) {
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
        StepGeometricRepresentationItem that = (StepGeometricRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepGeometricRepresentationItem{" + "id=" + id + "name=" + name + "}";
    }
}
