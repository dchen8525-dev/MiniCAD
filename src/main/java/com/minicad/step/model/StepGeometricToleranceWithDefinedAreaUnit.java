package com.minicad.step.model;

/**
 * Resolved GEOMETRIC_TOLERANCE_WITH_DEFINED_AREA_UNIT.
 * A geometric tolerance with a defined area unit for spatial application.
 */
public record StepGeometricToleranceWithDefinedAreaUnit(
    int id,
    String name,
    String toleranceType,
    Double magnitude,
    StepEntity magnitudeUnit,
    StepEntity tolerancedFeature,
    StepEntity areaUnit
) implements StepEntity {}
