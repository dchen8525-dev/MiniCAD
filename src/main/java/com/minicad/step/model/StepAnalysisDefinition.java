package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ANALYSIS_DEFINITION.
 * An analysis definition entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @param analysisType analysis variance type
 * @param analysisMethod analysis variance method
 * @param analysisInputs analysis variance inputs
 * @param analysisOutputs analysis variance expected outputs
 * @param analysisStatus analysis variance status
 */
public record StepAnalysisDefinition(
    int id,
    String name,
    String analysisType,
    String analysisMethod,
    List<String> analysisInputs,
    List<String> analysisOutputs,
    String analysisStatus) implements StepEntity {
}