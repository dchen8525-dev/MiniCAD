package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MAKE_FROM_FEATURE.
 * A manufacturing feature definition.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape shape aspect reference
 */
public final class StepMakeFromFeature extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;

    public StepMakeFromFeature(int id, String name, String description, StepEntity ofShape) {
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
