package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal COLOUR marker.
 *
 * @param id step id
 */
/**
 * Minimal COLOUR marker.
 *
 * @param id step id
 */
public final class StepColour implements StepEntity {
    private final int id;

    public StepColour(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepColour that = (StepColour) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StepColour{" + "id=" + id + "}";
    }
}
