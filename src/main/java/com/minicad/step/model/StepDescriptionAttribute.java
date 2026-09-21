package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DESCRIPTION_ATTRIBUTE metadata.
 *
 * @param id STEP instance id
 * @param attributeValue description value
 * @param describedItem described entity
 */
public final class StepDescriptionAttribute extends AbstractStepEntity {
    private final String attributeValue;
    private final StepEntity describedItem;

    public StepDescriptionAttribute(int id, String attributeValue, StepEntity describedItem) {
        super(id, "");
        this.attributeValue = attributeValue;
        this.describedItem = describedItem;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public StepEntity getDescribedItem() {
        return describedItem;
    }

    // Record-style accessor
    public StepEntity describedItem() {
        return describedItem;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("attributeValue", attributeValue);
        state.put("describedItem", describedItem);
        return state;
    }
}
