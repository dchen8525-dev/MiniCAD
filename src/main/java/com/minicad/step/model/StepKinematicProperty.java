package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved KINEMATIC_PROPERTY.
 * A kinematic property definition.
 */
public final class StepKinematicProperty extends AbstractStepEntity {
    private final String propertyType;
    private final StepEntity value;

    public StepKinematicProperty(int id, String name, String propertyType, StepEntity value) {
        super(id, name);
        this.propertyType = propertyType;
        this.value = value;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public StepEntity getValue() {
        return value;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "propertyType", propertyType, "value", value);
    }
}
