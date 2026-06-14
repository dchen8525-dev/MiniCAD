package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.workflow.StepPropertyDefinition;
import java.util.Objects;
/**
 * Minimal action property representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
/**
 * Minimal action property representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public final class StepActionPropertyRepresentation implements StepEntity {
    private final int id;
    private final StepPropertyDefinition definition;
    private final StepRepresentation usedRepresentation;

    public StepActionPropertyRepresentation(int id, StepPropertyDefinition definition, StepRepresentation usedRepresentation) {
        this.id = id;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
    }

    public int getId() {
        return id;
    }

    public StepPropertyDefinition getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionPropertyRepresentation that = (StepActionPropertyRepresentation) o;
        return id == that.id && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, definition, usedRepresentation);
    }

    @Override
    public String toString() {
        return "StepActionPropertyRepresentation{" + "id=" + id + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "}";
    }
}
