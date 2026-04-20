package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved USAGE_ASSOCIATION.
 */
public record StepUsageAssociation(
    int id,
    String name,
    StepEntity relatingUsage,
    StepEntity relatedUsage
) implements StepEntity {
}
