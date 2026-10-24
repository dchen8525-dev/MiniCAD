package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved CONVERSION_INSTANCE.
 * A conversion instance entity.
 *
 * @param id STEP instance id
 * @param name conversion instance name
 * @param conversionDefinition conversion variance definition reference
 * @param conversionInput conversion variance input value
 * @param conversionOutput conversion variance output value
 * @param conversionStatus conversion variance status
 */
public final class StepConversionInstance extends AbstractStepEntity {
    private final StepEntity conversionDefinition;
    private final double conversionInput;
    private final double conversionOutput;
    private final String conversionStatus;

    public StepConversionInstance(int id, String name, StepEntity conversionDefinition, double conversionInput, double conversionOutput, String conversionStatus) {
        super(id, name);
        this.conversionDefinition = conversionDefinition;
        this.conversionInput = conversionInput;
        this.conversionOutput = conversionOutput;
        this.conversionStatus = conversionStatus;
    }

    public StepEntity getConversionDefinition() {
        return conversionDefinition;
    }

    public double getConversionInput() {
        return conversionInput;
    }

    public double getConversionOutput() {
        return conversionOutput;
    }

    public String getConversionStatus() {
        return conversionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "conversionDefinition", conversionDefinition,
                "conversionInput", conversionInput, "conversionOutput", conversionOutput,
                "conversionStatus", conversionStatus);
    }
}
