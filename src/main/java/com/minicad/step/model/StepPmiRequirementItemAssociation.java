package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepPmiRequirementItemAssociation extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;
    private final StepEntity requirement;

    public StepPmiRequirementItemAssociation(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem, StepEntity requirement) {
        super(id, name);
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
        this.requirement = requirement;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("definition", definition);
        state.put("usedRepresentation", usedRepresentation);
        state.put("identifiedItem", identifiedItem);
        state.put("requirement", requirement);
        return state;
    }
}
