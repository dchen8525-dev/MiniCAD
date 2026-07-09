package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL.
 */
/**
 * Resolved DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL.
 */
public final class StepDraughtingPreDefinedTerminatorSymbol implements StepEntity {
    private final int id;
    private final String name;
    private final String identifier;

    public StepDraughtingPreDefinedTerminatorSymbol(int id, String name, String identifier) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDraughtingPreDefinedTerminatorSymbol that = (StepDraughtingPreDefinedTerminatorSymbol) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, identifier);
    }

    @Override
    public String toString() {
        return "StepDraughtingPreDefinedTerminatorSymbol{" + "id=" + id + "name=" + name + "identifier=" + identifier + "}";
    }
}
