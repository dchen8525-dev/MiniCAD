package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal PMI requirement item association.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 * @param requirement requirement object
 */
/**
 * Minimal PMI requirement item association.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 * @param requirement requirement object
 */
public final class StepPmiRequirementItemAssociation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;
    private final StepEntity requirement;

    public StepPmiRequirementItemAssociation(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem, StepEntity requirement) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
        this.requirement = requirement;
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

    public StepEntity getRequirement() {
        return requirement;
    }

    // Record-style accessors
    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity definition() {
        return definition;
    }

    public StepEntity requirement() {
        return requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPmiRequirementItemAssociation that = (StepPmiRequirementItemAssociation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation) && Objects.equals(identifiedItem, that.identifiedItem) && Objects.equals(requirement, that.requirement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition, usedRepresentation, identifiedItem, requirement);
    }

    @Override
    public String toString() {
        return "StepPmiRequirementItemAssociation{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "identifiedItem=" + identifiedItem + "requirement=" + requirement + "}";
    }
}
