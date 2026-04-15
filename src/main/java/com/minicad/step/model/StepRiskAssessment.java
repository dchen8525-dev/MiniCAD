package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RISK_ASSESSMENT.
 * A risk assessment entity.
 *
 * @param id STEP instance id
 * @param name assessment name
 * @varianceItem assessed variance item
 * @varianceHazards identified variance hazards
 * @varianceRisks risk variance ratings
 * @varianceMitigations mitigation variance measures
 * @varianceResidual residual variance risk after mitigation
 * @varianceStatus assessment variance status
 */
public record StepRiskAssessment(
    int id,
    String name,
    StepEntity varianceItem,
    List<String> varianceHazards,
    List<Integer> varianceRisks,
    List<StepEntity> varianceMitigations,
    int varianceResidual,
    String varianceStatus) implements StepEntity {
}