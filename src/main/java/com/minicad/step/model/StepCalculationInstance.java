package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCalculationInstance extends AbstractStepEntity {
    private final StepEntity calculationDefinition;
    private final List<Double> calculationInputValues;
    private final List<Double> calculationOutputValues;
    private final double calculationError;
    private final String calculationStatus;

    public StepCalculationInstance(int id, String name, StepEntity calculationDefinition, List<Double> calculationInputValues, List<Double> calculationOutputValues, double calculationError, String calculationStatus) {
        super(id, name);
        this.calculationDefinition = calculationDefinition;
        this.calculationInputValues = calculationInputValues == null ? null : java.util.List.copyOf(calculationInputValues);
        this.calculationOutputValues = calculationOutputValues == null ? null : java.util.List.copyOf(calculationOutputValues);
        this.calculationError = calculationError;
        this.calculationStatus = calculationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("calculationDefinition", calculationDefinition);
        state.put("calculationInputValues", calculationInputValues);
        state.put("calculationOutputValues", calculationOutputValues);
        state.put("calculationError", calculationError);
        state.put("calculationStatus", calculationStatus);
        return state;
    }
}
