package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal GENERAL_PROPERTY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGeneralProperty relating property
 * @param relatedGeneralProperty related property
 */
public final class StepGeneralPropertyRelationship extends AbstractStepEntity {
    private final String description;
    private final StepGeneralProperty relatingGeneralProperty;
    private final StepGeneralProperty relatedGeneralProperty;

    public StepGeneralPropertyRelationship(int id, String name, String description, StepGeneralProperty relatingGeneralProperty, StepGeneralProperty relatedGeneralProperty) {
        super(id, name);
        this.description = description;
        this.relatingGeneralProperty = relatingGeneralProperty;
        this.relatedGeneralProperty = relatedGeneralProperty;
    }

    public String getDescription() {
        return description;
    }

    public StepGeneralProperty getRelatingGeneralProperty() {
        return relatingGeneralProperty;
    }

    public StepGeneralProperty getRelatedGeneralProperty() {
        return relatedGeneralProperty;
    }

    // Record-style accessors
    public StepGeneralProperty relatingGeneralProperty() {
        return relatingGeneralProperty;
    }

    public StepGeneralProperty relatedGeneralProperty() {
        return relatedGeneralProperty;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingGeneralProperty", relatingGeneralProperty,
                "relatedGeneralProperty", relatedGeneralProperty);
    }
}
