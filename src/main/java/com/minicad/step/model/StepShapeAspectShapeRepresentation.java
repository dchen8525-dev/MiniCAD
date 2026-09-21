package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SHAPE_ASPECT_SHAPE_REPRESENTATION.
 * Shape representation for shape aspects.
 */
public final class StepShapeAspectShapeRepresentation extends AbstractStepEntity {
    private final StepEntity ofShape;

    public StepShapeAspectShapeRepresentation(int id, String name, StepEntity ofShape) {
        super(id, name);
        this.ofShape = ofShape;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("ofShape", ofShape);
        return state;
    }
}
