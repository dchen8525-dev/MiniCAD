package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_COLOUR.
 *
 * @param id step id
 * @param name predefined colour name
 */
/**
 * Minimal PRE_DEFINED_COLOUR.
 *
 * @param id step id
 * @param name predefined colour name
 */
public final class StepPreDefinedColour implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedColour(int id, String name) {
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
        StepPreDefinedColour that = (StepPreDefinedColour) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedColour{" + "id=" + id + "name=" + name + "}";
    }
}
