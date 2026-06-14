package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CALCULATION_DEFINITION.
 * A calculation definition entity.
 *
 * @param id STEP instance id
 * @param name calculation name
 * @param calculationType calculation variance type
 * @param calculationMethod calculation variance method
 * @param calculationInputs calculation variance input parameters
 * @param calculationOutputs calculation variance output parameters
 * @param calculationAccuracy calculation variance accuracy
 * @param calculationStatus calculation variance status
 */
/**
 * Resolved CALCULATION_DEFINITION.
 * A calculation definition entity.
 *
 * @param id STEP instance id
 * @param name calculation name
 * @param calculationType calculation variance type
 * @param calculationMethod calculation variance method
 * @param calculationInputs calculation variance input parameters
 * @param calculationOutputs calculation variance output parameters
 * @param calculationAccuracy calculation variance accuracy
 * @param calculationStatus calculation variance status
 */
public final class StepCalculationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String calculationType;
    private final String calculationMethod;
    private final List<String> calculationInputs;
    private final List<String> calculationOutputs;
    private final double calculationAccuracy;
    private final String calculationStatus;

    public StepCalculationDefinition(int id, String name, String calculationType, String calculationMethod, List<String> calculationInputs, List<String> calculationOutputs, double calculationAccuracy, String calculationStatus) {
        this.id = id;
        this.name = name;
        this.calculationType = calculationType;
        this.calculationMethod = calculationMethod;
        this.calculationInputs = calculationInputs == null ? null : java.util.List.copyOf(calculationInputs);
        this.calculationOutputs = calculationOutputs == null ? null : java.util.List.copyOf(calculationOutputs);
        this.calculationAccuracy = calculationAccuracy;
        this.calculationStatus = calculationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public List<String> getCalculationInputs() {
        return calculationInputs;
    }

    public List<String> getCalculationOutputs() {
        return calculationOutputs;
    }

    public double getCalculationAccuracy() {
        return calculationAccuracy;
    }

    public String getCalculationStatus() {
        return calculationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalculationDefinition that = (StepCalculationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(calculationType, that.calculationType) && Objects.equals(calculationMethod, that.calculationMethod) && Objects.equals(calculationInputs, that.calculationInputs) && Objects.equals(calculationOutputs, that.calculationOutputs) && calculationAccuracy == that.calculationAccuracy && Objects.equals(calculationStatus, that.calculationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calculationType, calculationMethod, calculationInputs, calculationOutputs, calculationAccuracy, calculationStatus);
    }

    @Override
    public String toString() {
        return "StepCalculationDefinition{" + "id=" + id + "name=" + name + "calculationType=" + calculationType + "calculationMethod=" + calculationMethod + "calculationInputs=" + calculationInputs + "calculationOutputs=" + calculationOutputs + "calculationAccuracy=" + calculationAccuracy + "calculationStatus=" + calculationStatus + "}";
    }
}