package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MACHINING_FEATURE_RELATIONSHIP.
 * A machining feature relationship entity.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param relatingFeature relating machining feature
 * @param relatedFeature related machining feature
 * @param relationshipType relationship type classification
 * @param description relationship description
 */
/**
 * Resolved MACHINING_FEATURE_RELATIONSHIP.
 * A machining feature relationship entity.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param relatingFeature relating machining feature
 * @param relatedFeature related machining feature
 * @param relationshipType relationship type classification
 * @param description relationship description
 */
public final class StepMachiningFeatureRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity relatingFeature;
    private final StepEntity relatedFeature;
    private final String relationshipType;
    private final String description;

    public StepMachiningFeatureRelationship(int id, String name, StepEntity relatingFeature, StepEntity relatedFeature, String relationshipType, String description) {
        this.id = id;
        this.name = name;
        this.relatingFeature = relatingFeature;
        this.relatedFeature = relatedFeature;
        this.relationshipType = relationshipType;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRelatingFeature() {
        return relatingFeature;
    }

    public StepEntity getRelatedFeature() {
        return relatedFeature;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningFeatureRelationship that = (StepMachiningFeatureRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(relatingFeature, that.relatingFeature) && Objects.equals(relatedFeature, that.relatedFeature) && Objects.equals(relationshipType, that.relationshipType) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, relatingFeature, relatedFeature, relationshipType, description);
    }

    @Override
    public String toString() {
        return "StepMachiningFeatureRelationship{" + "id=" + id + "name=" + name + "relatingFeature=" + relatingFeature + "relatedFeature=" + relatedFeature + "relationshipType=" + relationshipType + "description=" + description + "}";
    }
}