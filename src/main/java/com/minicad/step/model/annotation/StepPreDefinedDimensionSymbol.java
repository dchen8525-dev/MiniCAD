package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_DIMENSION_SYMBOL.
 *
 * @param id step id
 * @param name predefined dimension symbol name
 */
/**
 * Minimal PRE_DEFINED_DIMENSION_SYMBOL.
 *
 * @param id step id
 * @param name predefined dimension symbol name
 */
public final class StepPreDefinedDimensionSymbol implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedDimensionSymbol(int id, String name) {
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
        StepPreDefinedDimensionSymbol that = (StepPreDefinedDimensionSymbol) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedDimensionSymbol{" + "id=" + id + "name=" + name + "}";
    }
}
