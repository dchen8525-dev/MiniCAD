package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal typed measure-with-unit subtype.
 *
 * @param id STEP instance id
 * @param entityName specific STEP entity name such as LENGTH_MEASURE_WITH_UNIT
 * @param valueComponent numeric value
 * @param unitComponent referenced unit entity
 */
public final class StepTypedMeasureWithUnit extends AbstractStepEntity {
    private final String entityName;
    private final double valueComponent;
    private final StepEntity unitComponent;

    public StepTypedMeasureWithUnit(int id, String entityName, double valueComponent, StepEntity unitComponent) {
        super(id, "");
        this.entityName = entityName;
        this.valueComponent = valueComponent;
        this.unitComponent = unitComponent;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public double getValueComponent() {
        return valueComponent;
    }

    public StepEntity getUnitComponent() {
        return unitComponent;
    }

    // Record-style accessor
    public StepEntity unitComponent() {
        return unitComponent;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("valueComponent", valueComponent);
        state.put("unitComponent", unitComponent);
        return state;
    }
}
