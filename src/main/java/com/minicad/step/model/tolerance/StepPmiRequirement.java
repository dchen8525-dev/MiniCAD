package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved PMI_REQUIREMENT.
 */
public record StepPmiRequirement(
    int id,
    String name,
    String description,
    String requirementType
) implements StepEntity {
}
