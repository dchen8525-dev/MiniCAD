package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ATTRIBUTE_DEFINITION.
 * An attribute definition entity.
 *
 * @param id STEP instance id
 * @param name attribute name
 * @param attributeType attribute variance type
 * @param attributeDataType attribute variance data type
 * @param attributeRange attribute variance valid range
 * @param attributeDefault attribute variance default value
 * @param attributeStatus attribute variance status
 */
public final class StepAttributeDefinition extends AbstractStepEntity {
    private final String attributeType;
    private final String attributeDataType;
    private final List<String> attributeRange;
    private final String attributeDefault;
    private final String attributeStatus;

    public StepAttributeDefinition(int id, String name, String attributeType, String attributeDataType, List<String> attributeRange, String attributeDefault, String attributeStatus) {
        super(id, name);
        this.attributeType = attributeType;
        this.attributeDataType = attributeDataType;
        this.attributeRange = attributeRange == null ? null : java.util.List.copyOf(attributeRange);
        this.attributeDefault = attributeDefault;
        this.attributeStatus = attributeStatus;
    }

    public String getAttributeType() {
        return attributeType;
    }

    public String getAttributeDataType() {
        return attributeDataType;
    }

    public List<String> getAttributeRange() {
        return attributeRange;
    }

    public String getAttributeDefault() {
        return attributeDefault;
    }

    public String getAttributeStatus() {
        return attributeStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("attributeType", attributeType);
        state.put("attributeDataType", attributeDataType);
        state.put("attributeRange", attributeRange);
        state.put("attributeDefault", attributeDefault);
        state.put("attributeStatus", attributeStatus);
        return state;
    }
}
