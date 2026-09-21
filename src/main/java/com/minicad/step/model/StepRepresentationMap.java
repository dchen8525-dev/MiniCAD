package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped representation
 */
public final class StepRepresentationMap extends AbstractStepEntity {
    private final StepEntity mappedOrigin;
    private final StepRepresentation mappedRepresentation;

    public StepRepresentationMap(int id, StepEntity mappedOrigin, StepRepresentation mappedRepresentation) {
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
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity mappedOrigin() { return getMappedOrigin(); }
    public StepRepresentation mappedRepresentation() { return getMappedRepresentation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("mappedOrigin", mappedOrigin);
        state.put("mappedRepresentation", mappedRepresentation);
        return state;
    }
}
