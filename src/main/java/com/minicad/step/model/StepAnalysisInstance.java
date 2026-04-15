package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ANALYSIS_INSTANCE.
 * An analysis instance entity.
 *
 * @param id STEP instance id
 * @param name analysis instance name
 * @param analysisDefinition analysis variance definition reference
 * @param analysisState analysis variance state
 * @param analysisResults analysis variance results
 * @param analysisConclusions analysis variance conclusions
 * @param analysisStatus analysis variance status
 */
public record StepAnalysisInstance(
    int id,
    String name,
    StepEntity analysisDefinition,
    String analysisState,
    List<String> analysisResults,
    String analysisConclusions,
    String analysisStatus) implements StepEntity {
}