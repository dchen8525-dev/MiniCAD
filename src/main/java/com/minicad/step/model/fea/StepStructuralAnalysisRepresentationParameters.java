package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS.
 * Parameters for structural analysis representation.
 */
public record StepStructuralAnalysisRepresentationParameters(
    int id,
    String name,
    String parameterType) implements StepEntity {
}
