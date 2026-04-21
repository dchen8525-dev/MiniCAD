package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved GEOMETRIC_TOLERANCE_TARGET.
 * Specifies the target of a geometric tolerance application.
 */
public record StepGeometricToleranceTarget(
    int id,
    String name,
    StepEntity targetShape,
    double magnitude) implements StepEntity {
}
