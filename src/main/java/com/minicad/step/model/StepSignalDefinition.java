package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SIGNAL_DEFINITION.
 * A signal definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceSignal defined variance signal
 * @varianceType signal variance type (analog, digital, discrete)
 * @varianceRange signal variance range (min/max)
 * @varianceUnit signal variance unit
 * @varianceFrequency signal variance frequency
 * @varianceStatus definition variance status
 */
public final class StepSignalDefinition extends AbstractStepEntity {
    private final String varianceSignal;
    private final String varianceType;
    private final List<Double> varianceRange;
    private final StepEntity varianceUnit;
    private final double varianceFrequency;
    private final String varianceStatus;

    public StepSignalDefinition(int id, String name, String varianceSignal, String varianceType, List<Double> varianceRange, StepEntity varianceUnit, double varianceFrequency, String varianceStatus) {
        super(id, name);
        this.varianceSignal = varianceSignal;
        this.varianceType = varianceType;
        this.varianceRange = varianceRange == null ? null : java.util.List.copyOf(varianceRange);
        this.varianceUnit = varianceUnit;
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceSignal() {
        return varianceSignal;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public List<Double> getVarianceRange() {
        return varianceRange;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public double getVarianceFrequency() {
        return varianceFrequency;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSignal", varianceSignal);
        state.put("varianceType", varianceType);
        state.put("varianceRange", varianceRange);
        state.put("varianceUnit", varianceUnit);
        state.put("varianceFrequency", varianceFrequency);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
