package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DIMENSIONAL_LOCATION_WITH_PATH.
 * A dimensional location that includes a path definition for the measurement route.
 */
public final class StepDimensionalLocationWithPath extends AbstractStepEntity {
    private final String description;
    private final StepEntity toleratedShape;
    private final StepEntity path;

    public StepDimensionalLocationWithPath(int id, String name, String description, StepEntity toleratedShape, StepEntity path) {
        super(id, name);
        this.description = description;
        this.toleratedShape = toleratedShape;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getToleratedShape() {
        return toleratedShape;
    }

    public StepEntity getPath() {
        return path;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("toleratedShape", toleratedShape);
        state.put("path", path);
        return state;
    }
}
