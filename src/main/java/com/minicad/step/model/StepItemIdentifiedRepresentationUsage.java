package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepItemIdentifiedRepresentationUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;

    public StepItemIdentifiedRepresentationUsage(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem) {
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
    public String name() {
        return getName();
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
