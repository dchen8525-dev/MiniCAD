package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved STRESS_ANALYSIS.
 * Stress analysis type for FEA.
 */
public record StepStressAnalysis(
    int id,
    String name,
    String analysisType) implements StepEntity {
}
