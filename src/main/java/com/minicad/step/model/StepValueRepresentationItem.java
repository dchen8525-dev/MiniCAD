package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal value representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param valueType typed wrapper name
 * @param valueText unwrapped literal text
 */
public final class StepValueRepresentationItem extends AbstractStepEntity {
    private final String valueType;
    private final String valueText;

    public StepValueRepresentationItem(int id, String name, String valueType, String valueText) {
        super(id, name);
        this.valueType = valueType;
        this.valueText = valueText;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValueText() {
        return valueText;
    }

    // Record-style accessors
    public String valueType() {
        return valueType;
    }

    public String valueText() {
        return valueText;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("valueType", valueType);
        state.put("valueText", valueText);
        return state;
    }
}
