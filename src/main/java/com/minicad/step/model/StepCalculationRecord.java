package com.minicad.step.model.profile_analysis.analysis;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCalculationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String calculationType;
    private final List<Double> inputParameters;
    private final List<Double> varianceResults;
    private final String calculationMethod;
    private final StepEntity varianceUnits;
    private final List<String> varianceAssumptions;

    public StepCalculationRecord(int id, String name, String calculationType, List<Double> inputParameters, List<Double> varianceResults, String calculationMethod, StepEntity varianceUnits, List<String> varianceAssumptions) {
        this.id = id;
        this.name = name;
        this.calculationType = calculationType;
        this.inputParameters = inputParameters == null ? null : java.util.List.copyOf(inputParameters);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.calculationMethod = calculationMethod;
        this.varianceUnits = varianceUnits;
        this.varianceAssumptions = varianceAssumptions == null ? null : java.util.List.copyOf(varianceAssumptions);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCalculationRecord that = (StepCalculationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(calculationType, that.calculationType) && Objects.equals(inputParameters, that.inputParameters) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(calculationMethod, that.calculationMethod) && Objects.equals(varianceUnits, that.varianceUnits) && Objects.equals(varianceAssumptions, that.varianceAssumptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calculationType, inputParameters, varianceResults, calculationMethod, varianceUnits, varianceAssumptions);
    }

    @Override
    public String toString() {
        return "StepCalculationRecord{" + "id=" + id + "name=" + name + "calculationType=" + calculationType + "inputParameters=" + inputParameters + "varianceResults=" + varianceResults + "calculationMethod=" + calculationMethod + "varianceUnits=" + varianceUnits + "varianceAssumptions=" + varianceAssumptions + "}";
    }
}