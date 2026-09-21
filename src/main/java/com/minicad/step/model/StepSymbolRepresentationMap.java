package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SYMBOL_REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped symbol representation
 */
public final class StepSymbolRepresentationMap extends AbstractStepEntity {
    private final StepEntity mappedOrigin;
    private final StepRepresentation mappedRepresentation;

    public StepSymbolRepresentationMap(int id, StepEntity mappedOrigin, StepRepresentation mappedRepresentation) {
        super(id, "");
        this.mappedOrigin = mappedOrigin;
        this.mappedRepresentation = mappedRepresentation;
    }

    public StepEntity getMappedOrigin() {
        return mappedOrigin;
    }

    public StepRepresentation getMappedRepresentation() {
        return mappedRepresentation;
    }

    // Record-style accessors
    public StepEntity mappedOrigin() {
        return mappedOrigin;
    }

    public StepRepresentation mappedRepresentation() {
        return mappedRepresentation;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("mappedOrigin", mappedOrigin);
        state.put("mappedRepresentation", mappedRepresentation);
        return state;
    }
}
