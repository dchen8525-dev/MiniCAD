package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved BUY_FROM_USAGE_OPTION.
 */
public record StepBuyFromUsageOption(
    int id,
    String name,
    StepEntity supplier
) implements StepEntity {
}
