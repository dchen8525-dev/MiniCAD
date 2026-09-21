package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DIMENSIONAL_LOCATION.
 * A dimensional location between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatedShape referenced shape aspect
 */
public final class StepDimensionalLocation extends AbstractStepEntity {
    private final String description;
    private final StepEntity relatedShape;

    public StepDimensionalLocation(int id, String name, String description, StepEntity relatedShape) {
        super(id, name);
        this.description = description;
        this.relatedShape = relatedShape;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatedShape() {
        return relatedShape;
    }

    // Record-style accessor
    public StepEntity relatedShape() {
        return relatedShape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("relatedShape", relatedShape);
        return state;
    }
}
