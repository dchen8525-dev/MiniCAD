package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved STRUCTURAL_ANALYSIS_MODEL.
 * A structural analysis model for FEA.
 */
public record StepStructuralAnalysisModel(
    int id,
    String name,
    String analysisType,
    List<StepEntity> elements,
    List<StepEntity> loads,
    List<StepEntity> boundaryConditions) implements StepEntity {
}
