package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal shape definition representation link.
 *
 * @param id STEP instance id
 * @param definition referenced product definition shape
 * @param usedRepresentation referenced shape representation
 */
public final class StepShapeDefinitionRepresentation extends AbstractStepEntity {
    private final StepProductDefinitionShape definition;
    private final StepRepresentation usedRepresentation;

    public StepShapeDefinitionRepresentation(int id, StepProductDefinitionShape definition, StepRepresentation usedRepresentation) {
        super(id, "");
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
    }

    public StepProductDefinitionShape getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public StepProductDefinitionShape definition() { return definition; }
    public StepRepresentation usedRepresentation() { return usedRepresentation; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("definition", definition);
        state.put("usedRepresentation", usedRepresentation);
        return state;
    }
}
