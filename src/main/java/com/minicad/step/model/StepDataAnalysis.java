package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DATA_ANALYSIS.
 * A data analysis entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @varianceData analyzed variance data reference
 * @varianceMethod analysis variance method
 * @varianceResults analysis variance results
 * @varianceConclusions analysis variance conclusions
 * @varianceDate analysis variance date
 * @varianceStatus analysis variance status
 */
public record StepDataAnalysis(
    int id,
    String name,
    StepEntity varianceData,
    String varianceMethod,
    List<Double> varianceResults,
    String varianceConclusions,
    StepEntity varianceDate,
    String varianceStatus) implements StepEntity {
}