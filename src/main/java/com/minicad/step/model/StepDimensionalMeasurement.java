package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DIMENSIONAL_MEASUREMENT.
 * A dimensional measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param measurementGeometry geometry being measured
 * @param measurementType measurement type (linear, angular, radius)
 * @param nominalValue nominal dimension value
 * @param upperTolerance upper tolerance limit
 * @param lowerTolerance lower tolerance limit
 * @param measuredValue measured value
 * @param measurementUnit measurement unit reference
 */
public record StepDimensionalMeasurement(
    int id,
    String name,
    StepEntity measurementGeometry,
    String measurementType,
    double nominalValue,
    double upperTolerance,
    double lowerTolerance,
    double measuredValue,
    StepEntity measurementUnit) implements StepEntity {
}