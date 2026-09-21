package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ANNOTATION_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource symbol representation map
 * @param mappingTarget placement target
 */
public final class StepAnnotationSymbol extends AbstractStepEntity {
    private final StepSymbolRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepAnnotationSymbol(int id, String name, StepSymbolRepresentationMap mappingSource, StepEntity mappingTarget) {
        super(id, name);
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
    }

    public StepSymbolRepresentationMap getMappingSource() {
        return mappingSource;
    }

    public StepEntity getMappingTarget() {
        return mappingTarget;
    }

    // Record-style accessors
    public StepSymbolRepresentationMap mappingSource() {
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
