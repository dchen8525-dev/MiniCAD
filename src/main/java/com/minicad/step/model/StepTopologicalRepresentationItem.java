package com.minicad.step.model;

import java.util.Objects;

/**
 * Minimal topological representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
/**
 * Minimal topological representation item marker.
 *
 * @param id STEP instance id
 * @param name item name
 */
public final class StepTopologicalRepresentationItem implements StepEntity {
    private final int id;
    private final String name;

    public StepTopologicalRepresentationItem(int id, String name) {
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
        StepTopologicalRepresentationItem that = (StepTopologicalRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepTopologicalRepresentationItem{" + "id=" + id + "name=" + name + "}";
    }
}
