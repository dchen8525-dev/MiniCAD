package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepAnnotationOccurrenceRelationship extends AbstractStepEntity {
    private final String entityName;
    private final String description;
    private final StepEntity relatingAnnotationOccurrence;
    private final StepEntity relatedAnnotationOccurrence;

    public StepAnnotationOccurrenceRelationship(int id, String entityName, String name, String description, StepEntity relatingAnnotationOccurrence, StepEntity relatedAnnotationOccurrence) {
        super(id, name);
        this.entityName = entityName;
        this.description = description;
        this.relatingAnnotationOccurrence = relatingAnnotationOccurrence;
        this.relatedAnnotationOccurrence = relatedAnnotationOccurrence;
    }

    public String getEntityName() {
        return entityName;
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
        return getName();
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingAnnotationOccurrence", relatingAnnotationOccurrence);
        state.put("relatedAnnotationOccurrence", relatedAnnotationOccurrence);
        return state;
    }
}
