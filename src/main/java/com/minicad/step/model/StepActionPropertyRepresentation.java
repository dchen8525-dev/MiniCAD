package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal action property representation link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public final class StepActionPropertyRepresentation extends AbstractStepEntity {
    private final StepPropertyDefinition definition;
    private final StepRepresentation usedRepresentation;

    public StepActionPropertyRepresentation(int id, StepPropertyDefinition definition, StepRepresentation usedRepresentation) {
        super(id, "");
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("definition", definition);
        state.put("usedRepresentation", usedRepresentation);
        return state;
    }
}
