package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal ANNOTATION_OCCURRENCE_RELATIONSHIP.
 *
 * @param id STEP instance id
 * @param entityName STEP entity name
 * @param name relationship name
 * @param description relationship description
 * @param relatingAnnotationOccurrence source occurrence
 * @param relatedAnnotationOccurrence target occurrence
 */
/**
 * Minimal ANNOTATION_OCCURRENCE_RELATIONSHIP.
 *
 * @param id STEP instance id
 * @param entityName STEP entity name
 * @param name relationship name
 * @param description relationship description
 * @param relatingAnnotationOccurrence source occurrence
 * @param relatedAnnotationOccurrence target occurrence
 */
public final class StepAnnotationOccurrenceRelationship implements StepEntity {
    private final int id;
    private final String entityName;
    private final String name;
    private final String description;
    private final StepEntity relatingAnnotationOccurrence;
    private final StepEntity relatedAnnotationOccurrence;

    public StepAnnotationOccurrenceRelationship(int id, String entityName, String name, String description, StepEntity relatingAnnotationOccurrence, StepEntity relatedAnnotationOccurrence) {
        this.id = id;
        this.entityName = entityName;
        this.name = name;
        this.description = description;
        this.relatingAnnotationOccurrence = relatingAnnotationOccurrence;
        this.relatedAnnotationOccurrence = relatedAnnotationOccurrence;
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getRelatingAnnotationOccurrence() {
        return relatingAnnotationOccurrence;
    }

    public StepEntity getRelatedAnnotationOccurrence() {
        return relatedAnnotationOccurrence;
    }

    // Record-style accessors
    public String entityName() {
        return entityName;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepEntity relatingAnnotationOccurrence() {
        return relatingAnnotationOccurrence;
    }

    public StepEntity relatedAnnotationOccurrence() {
        return relatedAnnotationOccurrence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationOccurrenceRelationship that = (StepAnnotationOccurrenceRelationship) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingAnnotationOccurrence, that.relatingAnnotationOccurrence) && Objects.equals(relatedAnnotationOccurrence, that.relatedAnnotationOccurrence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, name, description, relatingAnnotationOccurrence, relatedAnnotationOccurrence);
    }

    @Override
    public String toString() {
        return "StepAnnotationOccurrenceRelationship{" + "id=" + id + "entityName=" + entityName + "name=" + name + "description=" + description + "relatingAnnotationOccurrence=" + relatingAnnotationOccurrence + "relatedAnnotationOccurrence=" + relatedAnnotationOccurrence + "}";
    }
}
