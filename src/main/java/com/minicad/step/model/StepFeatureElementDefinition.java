package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved FEATURE_ELEMENT_DEFINITION.
 */
/**
 * Resolved FEATURE_ELEMENT_DEFINITION.
 */
public final class StepFeatureElementDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String featureType;

    public StepFeatureElementDefinition(int id, String name, String featureType) {
        this.id = id;
        this.name = name;
        this.featureType = featureType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeatureElementDefinition that = (StepFeatureElementDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(featureType, that.featureType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, featureType);
    }

    @Override
    public String toString() {
        return "StepFeatureElementDefinition{" + "id=" + id + "name=" + name + "featureType=" + featureType + "}";
    }
}
