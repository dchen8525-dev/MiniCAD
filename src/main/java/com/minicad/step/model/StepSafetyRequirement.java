package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SAFETY_REQUIREMENT.
 * A safety requirement entity.
 *
 * @param id STEP instance id
 * @param name requirement name
 * @param requirementType requirement type (guarding, interlock, PPE)
 * @param requirementDescription requirement description
 * @variancePriority requirement variance priority
 * @param requirementStandard applicable safety standard
 * @varianceCompliance compliance variance status
 * @varianceActions required variance actions
 */
public record StepSafetyRequirement(
    int id,
    String name,
    String requirementType,
    String requirementDescription,
    int variancePriority,
    String requirementStandard,
    String varianceCompliance,
    List<StepEntity> varianceActions) implements StepEntity {
}