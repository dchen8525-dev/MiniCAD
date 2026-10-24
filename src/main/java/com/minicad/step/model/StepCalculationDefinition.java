package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepCalculationDefinition extends AbstractStepEntity {
    private final String calculationType;
    private final String calculationMethod;
    private final List<String> calculationInputs;
    private final List<String> calculationOutputs;
    private final double calculationAccuracy;
    private final String calculationStatus;

    public StepCalculationDefinition(int id, String name, String calculationType, String calculationMethod, List<String> calculationInputs, List<String> calculationOutputs, double calculationAccuracy, String calculationStatus) {
        super(id, name);
        this.calculationType = calculationType;
        this.calculationMethod = calculationMethod;
        this.calculationInputs = calculationInputs == null ? null : java.util.List.copyOf(calculationInputs);
        this.calculationOutputs = calculationOutputs == null ? null : java.util.List.copyOf(calculationOutputs);
        this.calculationAccuracy = calculationAccuracy;
        this.calculationStatus = calculationStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "calculationType", calculationType,
                "calculationMethod", calculationMethod, "calculationInputs", calculationInputs,
                "calculationOutputs", calculationOutputs, "calculationAccuracy", calculationAccuracy,
                "calculationStatus", calculationStatus);
    }
}
