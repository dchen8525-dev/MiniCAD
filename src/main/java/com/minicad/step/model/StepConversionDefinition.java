package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONVERSION_DEFINITION.
 * A conversion definition entity.
 *
 * @param id STEP instance id
 * @param name conversion name
 * @param conversionType conversion variance type
 * @param conversionSource conversion variance source unit/type
 * @param conversionTarget conversion variance target unit/type
 * @param conversionFactor conversion variance factor
 * @param conversionOffset conversion variance offset
 * @param conversionStatus conversion variance status
 */
public final class StepConversionDefinition extends AbstractStepEntity {
    private final String conversionType;
    private final StepEntity conversionSource;
    private final StepEntity conversionTarget;
    private final double conversionFactor;
    private final double conversionOffset;
    private final String conversionStatus;

    public StepConversionDefinition(int id, String name, String conversionType, StepEntity conversionSource, StepEntity conversionTarget, double conversionFactor, double conversionOffset, String conversionStatus) {
        super(id, name);
        this.conversionType = conversionType;
        this.conversionSource = conversionSource;
        this.conversionTarget = conversionTarget;
        this.conversionFactor = conversionFactor;
        this.conversionOffset = conversionOffset;
        this.conversionStatus = conversionStatus;
    }

    public String getConversionType() {
        return conversionType;
    }

    public StepEntity getConversionSource() {
        return conversionSource;
    }

    public StepEntity getConversionTarget() {
        return conversionTarget;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double getConversionOffset() {
        return conversionOffset;
    }

    public String getConversionStatus() {
        return conversionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("conversionType", conversionType);
        state.put("conversionSource", conversionSource);
        state.put("conversionTarget", conversionTarget);
        state.put("conversionFactor", conversionFactor);
        state.put("conversionOffset", conversionOffset);
        state.put("conversionStatus", conversionStatus);
        return state;
    }
}
