package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved STRUCT_ANALYSIS_MODEL.
 * A structural analysis model (AP209).
 */
public record StepStructAnalysisModel(
    int id,
    String name,
    String analysisType) implements StepEntity {
}
