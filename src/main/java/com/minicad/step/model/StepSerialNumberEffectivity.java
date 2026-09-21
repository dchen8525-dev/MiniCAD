package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SERIAL_NUMBER_EFFECTIVITY.
 */
public final class StepSerialNumberEffectivity extends AbstractStepEntity {
    private final String serialNumber;

    public StepSerialNumberEffectivity(int id, String name, String serialNumber) {
        super(id, name);
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("serialNumber", serialNumber);
        return state;
    }
}
