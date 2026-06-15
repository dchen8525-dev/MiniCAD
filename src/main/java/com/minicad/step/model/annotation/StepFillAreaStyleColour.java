package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal fill area style colour.
 *
 * @param id STEP instance id
 * @param name style name
 * @param colour referenced colour
 */
/**
 * Minimal fill area style colour.
 *
 * @param id STEP instance id
 * @param name style name
 * @param colour referenced colour
 */
public final class StepFillAreaStyleColour implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity colour;

    public StepFillAreaStyleColour(int id, String name, StepEntity colour) {
        this.id = id;
        this.name = name;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessor
    public StepEntity colour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyleColour that = (StepFillAreaStyleColour) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, colour);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyleColour{" + "id=" + id + "name=" + name + "colour=" + colour + "}";
    }
}
