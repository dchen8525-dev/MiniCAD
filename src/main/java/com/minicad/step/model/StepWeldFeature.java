package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WELD_FEATURE.
 * A weld feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType feature variance type
 * @param featureGeometry feature variance geometry reference
 * @param featureSpecification feature variance specification reference
 * @param featureStatus feature variance status
 */
public final class StepWeldFeature extends AbstractStepEntity {
    private final String featureType;
    private final StepEntity featureGeometry;
    private final StepEntity featureSpecification;
    private final String featureStatus;

    public StepWeldFeature(int id, String name, String featureType, StepEntity featureGeometry, StepEntity featureSpecification, String featureStatus) {
        super(id, name);
        this.featureType = featureType;
        this.featureGeometry = featureGeometry;
        this.featureSpecification = featureSpecification;
        this.featureStatus = featureStatus;
    }

    public String getFeatureType() {
        return featureType;
    }

    public StepEntity getFeatureGeometry() {
        return featureGeometry;
    }

    public StepEntity getFeatureSpecification() {
        return featureSpecification;
    }

    public String getFeatureStatus() {
        return featureStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("featureType", featureType);
        state.put("featureGeometry", featureGeometry);
        state.put("featureSpecification", featureSpecification);
        state.put("featureStatus", featureStatus);
        return state;
    }
}
