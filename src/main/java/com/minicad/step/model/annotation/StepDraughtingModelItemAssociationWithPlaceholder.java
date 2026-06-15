package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal draughting model item association carrying an annotation placeholder.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition association definition/select target
 * @param usedRepresentation draughting model representation
 * @param identifiedItem associated item
 * @param annotationPlaceholder annotation placeholder occurrence
 */
/**
 * Minimal draughting model item association carrying an annotation placeholder.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition association definition/select target
 * @param usedRepresentation draughting model representation
 * @param identifiedItem associated item
 * @param annotationPlaceholder annotation placeholder occurrence
 */
public final class StepDraughtingModelItemAssociationWithPlaceholder implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;
    private final StepAnnotationPlaceholderOccurrence annotationPlaceholder;

    public StepDraughtingModelItemAssociationWithPlaceholder(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem, StepAnnotationPlaceholderOccurrence annotationPlaceholder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
        this.annotationPlaceholder = annotationPlaceholder;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getDefinition() {
        return definition;
    }

    public StepRepresentation getUsedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    public StepAnnotationPlaceholderOccurrence getAnnotationPlaceholder() {
        return annotationPlaceholder;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepEntity definition() {
        return definition;
    }

    public StepRepresentation usedRepresentation() {
        return usedRepresentation;
    }

    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    public StepAnnotationPlaceholderOccurrence annotationPlaceholder() {
        return annotationPlaceholder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDraughtingModelItemAssociationWithPlaceholder that = (StepDraughtingModelItemAssociationWithPlaceholder) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(definition, that.definition) && Objects.equals(usedRepresentation, that.usedRepresentation) && Objects.equals(identifiedItem, that.identifiedItem) && Objects.equals(annotationPlaceholder, that.annotationPlaceholder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, definition, usedRepresentation, identifiedItem, annotationPlaceholder);
    }

    @Override
    public String toString() {
        return "StepDraughtingModelItemAssociationWithPlaceholder{" + "id=" + id + "name=" + name + "description=" + description + "definition=" + definition + "usedRepresentation=" + usedRepresentation + "identifiedItem=" + identifiedItem + "annotationPlaceholder=" + annotationPlaceholder + "}";
    }
}
