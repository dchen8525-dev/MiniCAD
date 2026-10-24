package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "ofShape", ofShape);
    }
}
