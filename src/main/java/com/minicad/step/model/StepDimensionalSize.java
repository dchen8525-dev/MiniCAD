package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DIMENSIONAL_SIZE.
 * A dimensional size of a shape aspect.
 *
 * @param id STEP instance id
 * @param name size name
 * @param description size description
 * @param ofShape shape aspect being measured
 */
public final class StepDimensionalSize extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;

    public StepDimensionalSize(int id, String name, String description, StepEntity ofShape) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getOfShape() {
        return ofShape;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        return state;
    }
}
