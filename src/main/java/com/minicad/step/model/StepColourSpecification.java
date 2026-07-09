package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal COLOUR_SPECIFICATION.
 *
 * @param id step id
 * @param name colour name
 */
/**
 * Minimal COLOUR_SPECIFICATION.
 *
 * @param id step id
 * @param name colour name
 */
public final class StepColourSpecification implements StepEntity {
    private final int id;
    private final String name;

    public StepColourSpecification(int id, String name) {
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
        StepColourSpecification that = (StepColourSpecification) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepColourSpecification{" + "id=" + id + "name=" + name + "}";
    }
}
