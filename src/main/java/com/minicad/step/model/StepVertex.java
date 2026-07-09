package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal vertex marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
/**
 * Minimal vertex marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public final class StepVertex implements StepEntity {
    private final int id;
    private final String name;

    public StepVertex(int id, String name) {
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
        StepVertex that = (StepVertex) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepVertex{" + "id=" + id + "name=" + name + "}";
    }
}
