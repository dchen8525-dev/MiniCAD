package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved THERMAL_ANALYSIS.
 * Thermal analysis type for FEA.
 */
public record StepThermalAnalysis(
    int id,
    String name,
    String thermalAnalysisType) implements StepEntity {
}
