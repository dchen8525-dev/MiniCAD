package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import com.minicad.step.model.workflow.StepPropertyDefinition;
import java.util.Objects;
/**
 * Minimal forward chaining rule premise link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
/**
 * Minimal forward chaining rule premise link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public final class StepForwardChainingRulePremise implements StepEntity {
    private final int id;
    private final StepPropertyDefinition definition;
    private final StepRepresentation usedRepresentation;

    public StepForwardChainingRulePremise(int id, StepPropertyDefinition definition, StepRepresentation usedRepresentation) {
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
    public StepPropertyDefinition definition() {
        return definition;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepForwardChainingRulePremise that = (StepForwardChainingRulePremise) o;
        return id == that.id && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, definition, usedRepresentation);
    }

    @Override
    public String toString() {
        return "StepForwardChainingRulePremise{" + "id=" + id + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "}";
    }
}
