package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "serialNumber", serialNumber);
    }
}
