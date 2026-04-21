package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved COMPOSITE_GROUP_TOLERANCE.
 * A composite tolerance that applies multiple tolerance requirements to a feature group.
 */
public record StepCompositeGroupTolerance(
    int id,
    String name,
    double magnitude,
    StepEntity toleratedShape) implements StepEntity {
}
