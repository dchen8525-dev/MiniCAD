package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved VOLUME_UNIT_WITH_UNIT.
 */
public final class StepVolumeUnitWithUnit extends AbstractStepEntity {
    private final StepEntity volumeUnit;
    private final StepEntity unitComponent;

    public StepVolumeUnitWithUnit(int id, String name, StepEntity volumeUnit, StepEntity unitComponent) {
        super(id, name);
        this.volumeUnit = volumeUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getVolumeUnit() {
        return volumeUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("volumeUnit", volumeUnit);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
