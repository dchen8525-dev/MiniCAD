package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("propertyType", propertyType);
        state.put("value", value);
        return state;
    }
}
