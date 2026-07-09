package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal edge marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
/**
 * Minimal edge marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public final class StepEdge implements StepEntity {
    private final int id;
    private final String name;

    public StepEdge(int id, String name) {
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
        StepEdge that = (StepEdge) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepEdge{" + "id=" + id + "name=" + name + "}";
    }
}
