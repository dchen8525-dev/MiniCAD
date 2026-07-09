package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal bounded-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
/**
 * Minimal bounded-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public final class StepBoundedCurve implements StepEntity {
    private final int id;
    private final String name;

    public StepBoundedCurve(int id, String name) {
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
        StepBoundedCurve that = (StepBoundedCurve) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepBoundedCurve{" + "id=" + id + "name=" + name + "}";
    }
}
