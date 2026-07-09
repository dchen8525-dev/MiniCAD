package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SYMBOL_COLOUR.
 *
 * @param id STEP instance id
 * @param colour referenced colour
 */
/**
 * Minimal SYMBOL_COLOUR.
 *
 * @param id STEP instance id
 * @param colour referenced colour
 */
public final class StepSymbolColour implements StepEntity {
    private final int id;
    private final StepEntity colour;

    public StepSymbolColour(int id, StepEntity colour) {
        this.id = id;
        this.colour = colour;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity colour() { return getColour(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSymbolColour that = (StepSymbolColour) o;
        return id == that.id && Objects.equals(colour, that.colour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, colour);
    }

    @Override
    public String toString() {
        return "StepSymbolColour{" + "id=" + id + "colour=" + colour + "}";
    }
}
