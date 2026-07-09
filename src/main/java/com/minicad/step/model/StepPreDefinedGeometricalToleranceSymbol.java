package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL.
 *
 * @param id step id
 * @param name predefined geometrical tolerance symbol name
 */
/**
 * Minimal PRE_DEFINED_GEOMETRICAL_TOLERANCE_SYMBOL.
 *
 * @param id step id
 * @param name predefined geometrical tolerance symbol name
 */
public final class StepPreDefinedGeometricalToleranceSymbol implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedGeometricalToleranceSymbol(int id, String name) {
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
        StepPreDefinedGeometricalToleranceSymbol that = (StepPreDefinedGeometricalToleranceSymbol) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedGeometricalToleranceSymbol{" + "id=" + id + "name=" + name + "}";
    }
}
