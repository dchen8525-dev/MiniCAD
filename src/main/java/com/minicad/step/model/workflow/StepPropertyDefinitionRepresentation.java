package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal property definition representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation property representation
 */
/**
 * Minimal property definition representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation property representation
 */
public final class StepPropertyDefinitionRepresentation implements StepEntity {
    private final int id;
    private final StepPropertyDefinition definition;
    private final StepRepresentation usedRepresentation;

    public StepPropertyDefinitionRepresentation(int id, StepPropertyDefinition definition, StepRepresentation usedRepresentation) {
        this.id = id;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepPropertyDefinition getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    // Record-style accessors
    public StepPropertyDefinition definition() { return getDefinition(); }
    public StepRepresentation usedRepresentation() { return getUsedRepresentation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPropertyDefinitionRepresentation that = (StepPropertyDefinitionRepresentation) o;
        return id == that.id && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, definition, usedRepresentation);
    }

    @Override
    public String toString() {
        return "StepPropertyDefinitionRepresentation{" + "id=" + id + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "}";
    }
}
