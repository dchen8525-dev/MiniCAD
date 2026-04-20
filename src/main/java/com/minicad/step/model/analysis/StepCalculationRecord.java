package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepCalculationRecord(
    int id,
    String name,
    String calculationType,
    List<Double> inputParameters,
    List<Double> varianceResults,
    String calculationMethod,
    StepEntity varianceUnits,
    List<String> varianceAssumptions) implements StepEntity {
}