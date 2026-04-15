package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SENSOR_FEATURE.
 * A sensor feature entity.
 *
 * @param id STEP instance id
 * @param name sensor name
 * @param sensorType sensor type (position, temperature, pressure, vision)
 * @param sensorGeometry sensor geometry representation
 * @param sensorPosition sensor position placement
 * @param measurementRange sensor measurement range
 * @varianceResolution sensor variance resolution
 * @param sensorInterface sensor interface specification
 */
public record StepSensorFeature(
    int id,
    String name,
    String sensorType,
    StepEntity sensorGeometry,
    StepEntity sensorPosition,
    List<Double> measurementRange,
    double varianceResolution,
    String sensorInterface) implements StepEntity {
}