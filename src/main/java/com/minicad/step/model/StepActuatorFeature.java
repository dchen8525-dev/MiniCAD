package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACTUATOR_FEATURE.
 * An actuator feature entity.
 *
 * @param id STEP instance id
 * @param name actuator name
 * @param actuatorType actuator type (linear, rotary, pneumatic, hydraulic)
 * @param actuatorGeometry actuator geometry representation
 * @param actuatorPosition actuator position placement
 * @param actuatorForce actuator force output
 * @param strokeLength actuator stroke length
 * @varianceSpeed actuator variance speed
 */
public record StepActuatorFeature(
    int id,
    String name,
    String actuatorType,
    StepEntity actuatorGeometry,
    StepEntity actuatorPosition,
    double actuatorForce,
    double strokeLength,
    double varianceSpeed) implements StepEntity {
}