package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal USER_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public final class StepUserDefinedTerminatorSymbol extends AbstractStepEntity {
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepUserDefinedTerminatorSymbol(int id, String name, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
        super(id, name);
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
    }

    public StepRepresentationMap getMappingSource() {
        return mappingSource;
    }

    public StepEntity getMappingTarget() {
        return mappingTarget;
    }

    // Record-style accessors
    public StepRepresentationMap mappingSource() {
        return mappingSource;
    }

    public StepEntity mappingTarget() {
        return mappingTarget;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("mappingSource", mappingSource);
        state.put("mappingTarget", mappingTarget);
        return state;
    }
}
