package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal draughting model item association.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition association definition/select target
 * @param usedRepresentation draughting model representation
 * @param identifiedItem associated item
 */
public final class StepDraughtingModelItemAssociation extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;

    public StepDraughtingModelItemAssociation(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem) {
        super(id, name);
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
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
    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity definition() {
        return definition;
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
        return state;
    }
}
