package com.minicad.step.model;

import com.minicad.step.model.StepEntity;

import com.minicad.step.model.StepRepresentation;
import java.util.Objects;
/**
 * Minimal shape definition representation link.
 *
 * @param id STEP instance id
 * @param definition referenced product definition shape
 * @param usedRepresentation referenced shape representation
 */
/**
 * Minimal shape definition representation link.
 *
 * @param id STEP instance id
 * @param definition referenced product definition shape
 * @param usedRepresentation referenced shape representation
 */
public final class StepShapeDefinitionRepresentation implements StepEntity {
    private final int id;
    private final StepProductDefinitionShape definition;
    private final StepRepresentation usedRepresentation;

    public StepShapeDefinitionRepresentation(int id, StepProductDefinitionShape definition, StepRepresentation usedRepresentation) {
        this.id = id;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
    }

    public int getId() {
        return id;
    }

    public StepProductDefinitionShape getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    // Record-style accessors
    public int id() { return id; }
    public String getName() { return ""; }
    public StepProductDefinitionShape definition() { return definition; }
    public StepRepresentation usedRepresentation() { return usedRepresentation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeDefinitionRepresentation that = (StepShapeDefinitionRepresentation) o;
        return id == that.id && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, definition, usedRepresentation);
    }

    @Override
    public String toString() {
        return "StepShapeDefinitionRepresentation{" + "id=" + id + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "}";
    }
}
