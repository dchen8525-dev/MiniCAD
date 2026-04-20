package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_MAXIMUM_TOLERANCE.
 * A geometric tolerance with a specified maximum tolerance limit.
 */
public record StepGeometricToleranceWithMaximumTolerance(
    int id,
    String name,
    String toleranceType,
    Double magnitude,
    StepEntity magnitudeUnit,
    StepEntity tolerancedFeature,
    Double maximumTolerance
) implements StepEntity {}
