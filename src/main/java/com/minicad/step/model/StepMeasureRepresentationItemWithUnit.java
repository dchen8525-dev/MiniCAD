package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MEASURE_REPRESENTATION_ITEM_WITH_UNIT.
 * A measure with unit as a representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param measureValue measure value
 * @param unit unit reference
 */
public final class StepMeasureRepresentationItemWithUnit extends AbstractStepEntity {
    private final double measureValue;
    private final StepEntity unit;

    public StepMeasureRepresentationItemWithUnit(int id, String name, double measureValue, StepEntity unit) {
        super(id, name);
        this.measureValue = measureValue;
        this.unit = unit;
    }

    public double getMeasureValue() {
        return measureValue;
    }

    public StepEntity getUnit() {
        return unit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("measureValue", measureValue);
        state.put("unit", unit);
        return state;
    }
}
