package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PARAMETER_DEFINITION.
 * A parameter definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceParameter defined variance parameter
 * @varianceType parameter variance type
 * @varianceDefaultValue default variance value
 * @varianceRange allowed variance range
 * @varianceUnit parameter variance unit
 * @varianceStatus definition variance status
 */
/**
 * Resolved PARAMETER_DEFINITION.
 * A parameter definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceParameter defined variance parameter
 * @varianceType parameter variance type
 * @varianceDefaultValue default variance value
 * @varianceRange allowed variance range
 * @varianceUnit parameter variance unit
 * @varianceStatus definition variance status
 */
public final class StepParameterDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceParameter;
    private final String varianceType;
    private final double varianceDefaultValue;
    private final List<Double> varianceRange;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepParameterDefinition(int id, String name, String varianceParameter, String varianceType, double varianceDefaultValue, List<Double> varianceRange, StepEntity varianceUnit, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceParameter = varianceParameter;
        this.varianceType = varianceType;
        this.varianceDefaultValue = varianceDefaultValue;
        this.varianceRange = varianceRange == null ? null : java.util.List.copyOf(varianceRange);
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceParameter() {
        return varianceParameter;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public double getVarianceDefaultValue() {
        return varianceDefaultValue;
    }

    public List<Double> getVarianceRange() {
        return varianceRange;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepParameterDefinition that = (StepParameterDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceParameter, that.varianceParameter) && Objects.equals(varianceType, that.varianceType) && varianceDefaultValue == that.varianceDefaultValue && Objects.equals(varianceRange, that.varianceRange) && Objects.equals(varianceUnit, that.varianceUnit) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceParameter, varianceType, varianceDefaultValue, varianceRange, varianceUnit, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepParameterDefinition{" + "id=" + id + "name=" + name + "varianceParameter=" + varianceParameter + "varianceType=" + varianceType + "varianceDefaultValue=" + varianceDefaultValue + "varianceRange=" + varianceRange + "varianceUnit=" + varianceUnit + "varianceStatus=" + varianceStatus + "}";
    }
}