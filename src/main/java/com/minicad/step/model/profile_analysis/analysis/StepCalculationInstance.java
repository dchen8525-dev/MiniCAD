package com.minicad.step.model.profile_analysis.analysis;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CALCULATION_INSTANCE.
 * A calculation instance entity.
 *
 * @param id STEP instance id
 * @param name calculation instance name
 * @param calculationDefinition calculation variance definition reference
 * @param calculationInputValues calculation variance input values
 * @param calculationOutputValues calculation variance output values
 * @param calculationError calculation variance error estimate
 * @param calculationStatus calculation variance status
 */
/**
 * Resolved CALCULATION_INSTANCE.
 * A calculation instance entity.
 *
 * @param id STEP instance id
 * @param name calculation instance name
 * @param calculationDefinition calculation variance definition reference
 * @param calculationInputValues calculation variance input values
 * @param calculationOutputValues calculation variance output values
 * @param calculationError calculation variance error estimate
 * @param calculationStatus calculation variance status
 */
public final class StepCalculationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity calculationDefinition;
    private final List<Double> calculationInputValues;
    private final List<Double> calculationOutputValues;
    private final double calculationError;
    private final String calculationStatus;

    public StepCalculationInstance(int id, String name, StepEntity calculationDefinition, List<Double> calculationInputValues, List<Double> calculationOutputValues, double calculationError, String calculationStatus) {
        this.id = id;
        this.name = name;
        this.calculationDefinition = calculationDefinition;
        this.calculationInputValues = calculationInputValues == null ? null : java.util.List.copyOf(calculationInputValues);
        this.calculationOutputValues = calculationOutputValues == null ? null : java.util.List.copyOf(calculationOutputValues);
        this.calculationError = calculationError;
        this.calculationStatus = calculationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCalculationDefinition() {
        return calculationDefinition;
    }

    public List<Double> getCalculationInputValues() {
        return calculationInputValues;
    }

    public List<Double> getCalculationOutputValues() {
        return calculationOutputValues;
    }

    public double getCalculationError() {
        return calculationError;
    }

    public String getCalculationStatus() {
        return calculationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalculationInstance that = (StepCalculationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(calculationDefinition, that.calculationDefinition) && Objects.equals(calculationInputValues, that.calculationInputValues) && Objects.equals(calculationOutputValues, that.calculationOutputValues) && calculationError == that.calculationError && Objects.equals(calculationStatus, that.calculationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calculationDefinition, calculationInputValues, calculationOutputValues, calculationError, calculationStatus);
    }

    @Override
    public String toString() {
        return "StepCalculationInstance{" + "id=" + id + "name=" + name + "calculationDefinition=" + calculationDefinition + "calculationInputValues=" + calculationInputValues + "calculationOutputValues=" + calculationOutputValues + "calculationError=" + calculationError + "calculationStatus=" + calculationStatus + "}";
    }
}