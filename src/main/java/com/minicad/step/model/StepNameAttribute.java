package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal NAME_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue name value
 * @param namedItem named entity
 */
public final class StepNameAttribute extends AbstractStepEntity {
    private final String attributeValue;
    private final StepEntity namedItem;

    public StepNameAttribute(int id, String attributeValue, StepEntity namedItem) {
        super(id, "");
        this.attributeValue = attributeValue;
        this.namedItem = namedItem;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public StepEntity getNamedItem() {
        return namedItem;
    }

    public String getName() {
        return attributeValue != null ? attributeValue : "";
    }

    // Record-style accessor
    public StepEntity namedItem() {
        return namedItem;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("attributeValue", attributeValue);
        state.put("namedItem", namedItem);
        return state;
    }
}
