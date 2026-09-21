package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MASS_UNIT_WITH_UNIT.
 */
public final class StepMassUnitWithUnit extends AbstractStepEntity {
    private final StepEntity massUnit;
    private final StepEntity unitComponent;

    public StepMassUnitWithUnit(int id, String name, StepEntity massUnit, StepEntity unitComponent) {
        super(id, name);
        this.massUnit = massUnit;
        this.unitComponent = unitComponent;
    }

    public StepEntity getMassUnit() {
        return massUnit;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("massUnit", massUnit);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
