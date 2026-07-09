package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFastenerFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String featureType;
    private final StepEntity featureGeometry;
    private final StepEntity featureSpecification;
    private final String featureStatus;

    public StepFastenerFeature(int id, String name, String featureType, StepEntity featureGeometry, StepEntity featureSpecification, String featureStatus) {
        this.id = id;
        this.name = name;
        this.featureType = featureType;
        this.featureGeometry = featureGeometry;
        this.featureSpecification = featureSpecification;
        this.featureStatus = featureStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFastenerFeature that = (StepFastenerFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(featureType, that.featureType) && Objects.equals(featureGeometry, that.featureGeometry) && Objects.equals(featureSpecification, that.featureSpecification) && Objects.equals(featureStatus, that.featureStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, featureType, featureGeometry, featureSpecification, featureStatus);
    }

    @Override
    public String toString() {
        return "StepFastenerFeature{" + "id=" + id + "name=" + name + "featureType=" + featureType + "featureGeometry=" + featureGeometry + "featureSpecification=" + featureSpecification + "featureStatus=" + featureStatus + "}";
    }
}