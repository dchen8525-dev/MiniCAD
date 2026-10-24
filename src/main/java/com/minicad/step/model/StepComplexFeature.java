package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved COMPLEX_FEATURE.
 * A complex feature entity combining multiple features.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param componentFeatures component features
 * @param featureType complex feature type classification
 * @param position feature position placement
 * @param orientation feature orientation
 */
public final class StepComplexFeature extends AbstractStepEntity {
    private final List<StepEntity> componentFeatures;
    private final String featureType;
    private final StepEntity position;
    private final StepEntity orientation;

    public StepComplexFeature(int id, String name, List<StepEntity> componentFeatures, String featureType, StepEntity position, StepEntity orientation) {
        super(id, name);
        this.componentFeatures = componentFeatures == null ? null : java.util.List.copyOf(componentFeatures);
        this.featureType = featureType;
        this.position = position;
        this.orientation = orientation;
    }

    public List<StepEntity> getComponentFeatures() {
        return componentFeatures;
    }

    public String getFeatureType() {
        return featureType;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getOrientation() {
        return orientation;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "componentFeatures", componentFeatures,
                "featureType", featureType, "position", position, "orientation", orientation);
    }
}
