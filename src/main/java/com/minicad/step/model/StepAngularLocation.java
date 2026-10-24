package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved ANGULAR_LOCATION.
 * Location defined by an angular relationship between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatingShape relating shape aspect
 * @param relatedShape related shape aspect
 */
public final class StepAngularLocation extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatingShape;
    private final StepEntity relatedShape;

    public StepAngularLocation(int id, String name, String description, StepEntity relatingShape, StepEntity relatedShape) {
        super(id, name);
        this.description = description;
        this.relatingShape = relatingShape;
        this.relatedShape = relatedShape;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingShape() {
        return relatingShape;
    }

    public StepEntity getRelatedShape() {
        return relatedShape;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "relatingShape", relatingShape,
                "relatedShape", relatedShape);
    }
}
