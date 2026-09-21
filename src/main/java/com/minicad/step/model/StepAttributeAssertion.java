package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal attribute assertion link.
 *
 * @param id STEP instance id
 * @param definition property definition
 * @param usedRepresentation referenced representation
 */
public final class StepAttributeAssertion extends AbstractStepEntity {
    private final StepPropertyDefinition definition;
    private final StepRepresentation usedRepresentation;

    public StepAttributeAssertion(int id, StepPropertyDefinition definition, StepRepresentation usedRepresentation) {
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
    public StepPropertyDefinition definition() {
        return definition;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("definition", definition);
        state.put("usedRepresentation", usedRepresentation);
        return state;
    }
}
