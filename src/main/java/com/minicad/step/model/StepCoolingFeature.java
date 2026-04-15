package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COOLING_FEATURE.
 * A cooling feature entity.
 *
 * @param id STEP instance id
 * @param name cooling name
 * @param coolingType cooling type (air, liquid, refrigeration)
 * @param coolingGeometry cooling geometry representation
 * @param coolingCapacity cooling capacity specification
 * @param coolantType coolant type specification
 * @param coolingChannels cooling channel features
 * @param operatingTemperature operating temperature range
 */
public record StepCoolingFeature(
    int id,
    String name,
    String coolingType,
    StepEntity coolingGeometry,
    double coolingCapacity,
    StepEntity coolantType,
    List<StepEntity> coolingChannels,
    List<Double> operatingTemperature) implements StepEntity {
}