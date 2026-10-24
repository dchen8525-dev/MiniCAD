package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal USER_DEFINED_MARKER.
 *
 * @param id STEP instance id
 * @param name marker name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public final class StepUserDefinedMarker extends AbstractStepEntity {
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepUserDefinedMarker(int id, String name, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "mappingSource", mappingSource,
                "mappingTarget", mappingTarget);
    }
}
