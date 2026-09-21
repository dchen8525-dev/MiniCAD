package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal conversion-based unit with offset definition.
 *
 * @param id STEP instance id
 * @param name unit label
 * @param unitKind derived unit kind such as THERMODYNAMIC_TEMPERATURE_UNIT
 * @param conversionFactor referenced conversion factor
 * @param conversionOffset scalar offset
 */
public final class StepConversionBasedUnitWithOffset extends AbstractStepEntity {
    private final String unitKind;
    private final StepMeasureWithUnit conversionFactor;
    private final double conversionOffset;

    public StepConversionBasedUnitWithOffset(int id, String name, String unitKind, StepMeasureWithUnit conversionFactor, double conversionOffset) {
        super(id, name);
        this.unitKind = unitKind;
        this.conversionFactor = conversionFactor;
        this.conversionOffset = conversionOffset;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public StepMeasureWithUnit getConversionFactor() {
        return conversionFactor;
    }

    public double getConversionOffset() {
        return conversionOffset;
    }

    // Record-style accessor
    public StepMeasureWithUnit conversionFactor() {
        return conversionFactor;
    }

    public double conversionOffset() {
        return conversionOffset;
    }

    public String unitKind() {
        return unitKind;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("unitKind", unitKind);
        state.put("conversionFactor", conversionFactor);
        state.put("conversionOffset", conversionOffset);
        return state;
    }
}
