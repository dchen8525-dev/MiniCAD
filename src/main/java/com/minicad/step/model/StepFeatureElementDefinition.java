package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FEATURE_ELEMENT_DEFINITION.
 */
public final class StepFeatureElementDefinition extends AbstractStepEntity {
    private final String featureType;

    public StepFeatureElementDefinition(int id, String name, String featureType) {
        super(id, name);
        this.featureType = featureType;
    }

    public String getFeatureType() {
        return featureType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("featureType", featureType);
        return state;
    }
}
