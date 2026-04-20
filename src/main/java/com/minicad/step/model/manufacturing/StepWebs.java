package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved WEBS.
 */
public record StepWebs(
    int id,
    String name,
    double thickness
) implements StepEntity {
}
