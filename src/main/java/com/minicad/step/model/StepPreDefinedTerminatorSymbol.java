package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRE_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id step id
 * @param name predefined terminator symbol name
 */
/**
 * Minimal PRE_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id step id
 * @param name predefined terminator symbol name
 */
public final class StepPreDefinedTerminatorSymbol implements StepEntity {
    private final int id;
    private final String name;

    public StepPreDefinedTerminatorSymbol(int id, String name) {
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
        StepPreDefinedTerminatorSymbol that = (StepPreDefinedTerminatorSymbol) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepPreDefinedTerminatorSymbol{" + "id=" + id + "name=" + name + "}";
    }
}
