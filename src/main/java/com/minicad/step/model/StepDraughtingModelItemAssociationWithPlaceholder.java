package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepDraughtingModelItemAssociationWithPlaceholder extends AbstractStepEntity {
    private final String description;
    private final StepEntity definition;
    private final StepRepresentation usedRepresentation;
    private final StepEntity identifiedItem;
    private final StepAnnotationPlaceholderOccurrence annotationPlaceholder;

    public StepDraughtingModelItemAssociationWithPlaceholder(int id, String name, String description, StepEntity definition, StepRepresentation usedRepresentation, StepEntity identifiedItem, StepAnnotationPlaceholderOccurrence annotationPlaceholder) {
        super(id, name);
        this.description = description;
        this.definition = definition;
        this.usedRepresentation = usedRepresentation;
        this.identifiedItem = identifiedItem;
        this.annotationPlaceholder = annotationPlaceholder;
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
        return getName();
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("definition", definition);
        state.put("usedRepresentation", usedRepresentation);
        state.put("identifiedItem", identifiedItem);
        state.put("annotationPlaceholder", annotationPlaceholder);
        return state;
    }
}
