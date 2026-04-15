package com.minicad.step.model;

import java.util.List;

/**
 * Resolved GEOMETRIC_MEASUREMENT.
 * A geometric measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param measurementGeometry geometry being measured
 * @param geometricType geometric measurement type (flatness, roundness, position)
 * @param toleranceZone tolerance zone specification
 * @param measuredValue measured deviation value
 * @param measurementPoints measurement points used
 * @param passFailStatus pass/fail status result
 */
public record StepGeometricMeasurement(
    int id,
    String name,
    StepEntity measurementGeometry,
    String geometricType,
    StepEntity toleranceZone,
    double measuredValue,
    List<StepEntity> measurementPoints,
    String passFailStatus) implements StepEntity {
}