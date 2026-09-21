package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ATTRIBUTE_INSTANCE.
 * An attribute instance entity.
 *
 * @param id STEP instance id
 * @param name attribute instance name
 * @param attributeDefinition attribute variance definition reference
 * @param attributeValue attribute variance current value
 * @param attributeStatus attribute variance status
 */
public final class StepAttributeInstance extends AbstractStepEntity {
    private final StepEntity attributeDefinition;
    private final String attributeValue;
    private final String attributeStatus;

    public StepAttributeInstance(int id, String name, StepEntity attributeDefinition, String attributeValue, String attributeStatus) {
        super(id, name);
        this.attributeDefinition = attributeDefinition;
        this.attributeValue = attributeValue;
        this.attributeStatus = attributeStatus;
    }

    public StepEntity getAttributeDefinition() {
        return attributeDefinition;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public String getAttributeStatus() {
        return attributeStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("attributeDefinition", attributeDefinition);
        state.put("attributeValue", attributeValue);
        state.put("attributeStatus", attributeStatus);
        return state;
    }
}
