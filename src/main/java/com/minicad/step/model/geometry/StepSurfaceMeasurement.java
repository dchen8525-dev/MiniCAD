package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SURFACE_MEASUREMENT.
 * A surface measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param surfaceGeometry surface being measured
 * @param roughnessParameters roughness parameters (Ra, Rz, Rq)
 * @param measuredValues measured roughness values
 * @param measurementMethod measurement method specification
 * @param measurementArea measurement area/location
 * @param passFailStatus pass/fail status result
 */
public record StepSurfaceMeasurement(
    int id,
    String name,
    StepEntity surfaceGeometry,
    List<String> roughnessParameters,
    List<Double> measuredValues,
    String measurementMethod,
    StepEntity measurementArea,
    String passFailStatus) implements StepEntity {
}