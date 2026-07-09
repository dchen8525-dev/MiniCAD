package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepComplexFeature implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> componentFeatures;
    private final String featureType;
    private final StepEntity position;
    private final StepEntity orientation;

    public StepComplexFeature(int id, String name, List<StepEntity> componentFeatures, String featureType, StepEntity position, StepEntity orientation) {
        this.id = id;
        this.name = name;
        this.componentFeatures = componentFeatures == null ? null : java.util.List.copyOf(componentFeatures);
        this.featureType = featureType;
        this.position = position;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComplexFeature that = (StepComplexFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(componentFeatures, that.componentFeatures) && Objects.equals(featureType, that.featureType) && Objects.equals(position, that.position) && Objects.equals(orientation, that.orientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, componentFeatures, featureType, position, orientation);
    }

    @Override
    public String toString() {
        return "StepComplexFeature{" + "id=" + id + "name=" + name + "componentFeatures=" + componentFeatures + "featureType=" + featureType + "position=" + position + "orientation=" + orientation + "}";
    }
}