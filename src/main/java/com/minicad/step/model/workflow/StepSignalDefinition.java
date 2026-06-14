package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSignalDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceSignal;
    private final String varianceType;
    private final List<Double> varianceRange;
    private final StepEntity varianceUnit;
    private final double varianceFrequency;
    private final String varianceStatus;

    public StepSignalDefinition(int id, String name, String varianceSignal, String varianceType, List<Double> varianceRange, StepEntity varianceUnit, double varianceFrequency, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSignal = varianceSignal;
        this.varianceType = varianceType;
        this.varianceRange = varianceRange == null ? null : java.util.List.copyOf(varianceRange);
        this.varianceUnit = varianceUnit;
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSignalDefinition that = (StepSignalDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSignal, that.varianceSignal) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceRange, that.varianceRange) && Objects.equals(varianceUnit, that.varianceUnit) && varianceFrequency == that.varianceFrequency && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSignal, varianceType, varianceRange, varianceUnit, varianceFrequency, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSignalDefinition{" + "id=" + id + "name=" + name + "varianceSignal=" + varianceSignal + "varianceType=" + varianceType + "varianceRange=" + varianceRange + "varianceUnit=" + varianceUnit + "varianceFrequency=" + varianceFrequency + "varianceStatus=" + varianceStatus + "}";
    }
}