package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MEASUREMENT_POINT.
 * A measurement point entity.
 *
 * @param id STEP instance id
 * @param name point name
 * @param pointPosition measurement point position geometry
 * @param measurementType measurement type (dimensional, geometric, surface)
 * @param measurementDirection measurement direction vector
 * @param toleranceReference tolerance reference for this point
 * @param nominalValue nominal value for measurement
 * @param measurementSequence measurement sequence order
 */
public record StepMeasurementPoint(
    int id,
    String name,
    StepEntity pointPosition,
    String measurementType,
    StepEntity measurementDirection,
    StepEntity toleranceReference,
    double nominalValue,
    int measurementSequence) implements StepEntity {
}