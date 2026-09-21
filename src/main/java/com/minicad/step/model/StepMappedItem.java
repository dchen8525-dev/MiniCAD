package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal MAPPED_ITEM.
 *
 * @param id step id
 * @param mappingSource representation map
 * @param mappingTarget target representation item
 */
public final class StepMappedItem extends AbstractStepEntity {
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepMappedItem(int id, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
        super(id, "");
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
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepRepresentationMap mappingSource() { return getMappingSource(); }
    public StepEntity mappingTarget() { return getMappingTarget(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("mappingSource", mappingSource);
        state.put("mappingTarget", mappingTarget);
        return state;
    }
}
