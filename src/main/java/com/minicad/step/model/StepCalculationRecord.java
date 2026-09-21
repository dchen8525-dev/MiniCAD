package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CALCULATION_RECORD.
 * A calculation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @param calculationType calculation type (stress, deflection, thermal)
 * @param inputParameters calculation input parameters
 * @varianceResults calculation variance results
 * @param calculationMethod calculation method used
 * @varianceUnits calculation variance units
 * @varianceAssumptions calculation variance assumptions
 */
public final class StepCalculationRecord extends AbstractStepEntity {
    private final String calculationType;
    private final List<Double> inputParameters;
    private final List<Double> varianceResults;
    private final String calculationMethod;
    private final StepEntity varianceUnits;
    private final List<String> varianceAssumptions;

    public StepCalculationRecord(int id, String name, String calculationType, List<Double> inputParameters, List<Double> varianceResults, String calculationMethod, StepEntity varianceUnits, List<String> varianceAssumptions) {
        super(id, name);
        this.calculationType = calculationType;
        this.inputParameters = inputParameters == null ? null : java.util.List.copyOf(inputParameters);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.calculationMethod = calculationMethod;
        this.varianceUnits = varianceUnits;
        this.varianceAssumptions = varianceAssumptions == null ? null : java.util.List.copyOf(varianceAssumptions);
    }

    public String getCalculationType() {
        return calculationType;
    }

    public List<Double> getInputParameters() {
        return inputParameters;
    }

    public List<Double> getVarianceResults() {
        return varianceResults;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public StepEntity getVarianceUnits() {
        return varianceUnits;
    }

    public List<String> getVarianceAssumptions() {
        return varianceAssumptions;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("calculationType", calculationType);
        state.put("inputParameters", inputParameters);
        state.put("varianceResults", varianceResults);
        state.put("calculationMethod", calculationMethod);
        state.put("varianceUnits", varianceUnits);
        state.put("varianceAssumptions", varianceAssumptions);
        return state;
    }
}
