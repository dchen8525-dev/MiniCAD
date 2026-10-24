package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal GENERAL_PROPERTY metadata.
 *
 * @param id STEP instance id
 * @param propertyId property identifier
 * @param name property name
 * @param description property description
 */
public final class StepGeneralProperty extends AbstractStepEntity {
    private final String propertyId;
    private final String description;

    public StepGeneralProperty(int id, String propertyId, String name, String description) {
        super(id, name);
        this.propertyId = propertyId;
        this.description = description;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "propertyId", propertyId, "name", getName(), "description", description);
    }
}
