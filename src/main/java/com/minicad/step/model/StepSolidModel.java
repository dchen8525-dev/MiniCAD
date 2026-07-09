package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal solid-model marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
/**
 * Minimal solid-model marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public final class StepSolidModel implements StepEntity {
    private final int id;
    private final String name;

    public StepSolidModel(int id, String name) {
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
        StepSolidModel that = (StepSolidModel) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepSolidModel{" + "id=" + id + "name=" + name + "}";
    }
}
