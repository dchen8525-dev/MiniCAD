package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SHAPE_REPRESENTATION_TRANSFORMATION.
 * A transformation between shape representations.
 */
public final class StepShapeRepresentationTransformation extends AbstractStepEntity {
    private final String transformationType;
    private final StepEntity transformation;

    public StepShapeRepresentationTransformation(int id, String name, String transformationType, StepEntity transformation) {
        super(id, name);
        this.transformationType = transformationType;
        this.transformation = transformation;
    }

    public String getTransformationType() {
        return transformationType;
    }

    public StepEntity getTransformation() {
        return transformation;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transformationType", transformationType);
        state.put("transformation", transformation);
        return state;
    }
}
