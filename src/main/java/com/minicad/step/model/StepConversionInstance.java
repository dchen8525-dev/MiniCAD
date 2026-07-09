package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
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
public final class StepConversionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity conversionDefinition;
    private final double conversionInput;
    private final double conversionOutput;
    private final String conversionStatus;

    public StepConversionInstance(int id, String name, StepEntity conversionDefinition, double conversionInput, double conversionOutput, String conversionStatus) {
        this.id = id;
        this.name = name;
        this.conversionDefinition = conversionDefinition;
        this.conversionInput = conversionInput;
        this.conversionOutput = conversionOutput;
        this.conversionStatus = conversionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConversionInstance that = (StepConversionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(conversionDefinition, that.conversionDefinition) && conversionInput == that.conversionInput && conversionOutput == that.conversionOutput && Objects.equals(conversionStatus, that.conversionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, conversionDefinition, conversionInput, conversionOutput, conversionStatus);
    }

    @Override
    public String toString() {
        return "StepConversionInstance{" + "id=" + id + "name=" + name + "conversionDefinition=" + conversionDefinition + "conversionInput=" + conversionInput + "conversionOutput=" + conversionOutput + "conversionStatus=" + conversionStatus + "}";
    }
}