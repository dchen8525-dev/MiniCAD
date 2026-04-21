package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION.
 * A structural analysis representation.
 */
public record StepStructuralAnalysisRepresentation(
    int id,
    String name,
    String analysisType,
    List<StepEntity> items) implements StepEntity {
}
