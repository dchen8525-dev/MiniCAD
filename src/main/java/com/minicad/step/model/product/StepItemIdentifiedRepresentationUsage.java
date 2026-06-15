package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal link from a representation to an identified item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 */
/**
 * Minimal link from a representation to an identified item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 */
public final class StepItemIdentifiedRepresentationUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;

    public StepItemIdentifiedRepresentationUsage(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepEntity definition() {
        return definition;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepItemIdentifiedRepresentationUsage that = (StepItemIdentifiedRepresentationUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation) && Objects.equals(identifiedItem, that.identifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition, usedRepresentation, identifiedItem);
    }

    @Override
    public String toString() {
        return "StepItemIdentifiedRepresentationUsage{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "identifiedItem=" + identifiedItem + "}";
    }
}
