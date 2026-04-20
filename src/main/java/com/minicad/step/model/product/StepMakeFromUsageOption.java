package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MAKE_FROM_USAGE_OPTION.
 * A manufacturing usage option.
 */
public record StepMakeFromUsageOption(
    int id,
    String name,
    String description,
    StepEntity usage) implements StepEntity {
}
