package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved DATUM_FEATURE.
 * A datum feature used for geometric dimensioning and tolerancing.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape product definition shape
 */
public final class StepDatumFeature extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;

    public StepDatumFeature(int id, String name, String description, StepEntity ofShape) {
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
