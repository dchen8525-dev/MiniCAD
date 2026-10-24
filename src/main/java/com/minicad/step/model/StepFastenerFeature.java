package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FASTENER_FEATURE.
 * A fastener feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType feature variance type
 * @param featureGeometry feature variance geometry reference
 * @param featureSpecification feature variance specification reference
 * @param featureStatus feature variance status
 */
public final class StepFastenerFeature extends AbstractStepEntity {
    private final String featureType;
    private final StepEntity featureGeometry;
    private final StepEntity featureSpecification;
    private final String featureStatus;

    public StepFastenerFeature(int id, String name, String featureType, StepEntity featureGeometry, StepEntity featureSpecification, String featureStatus) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "featureType", featureType,
                "featureGeometry", featureGeometry, "featureSpecification", featureSpecification,
                "featureStatus", featureStatus);
    }
}
