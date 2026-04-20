package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HEATING_FEATURE.
 * A heating feature entity.
 *
 * @param id STEP instance id
 * @param name heating name
 * @param heatingType heating type (electric, gas, induction)
 * @param heatingGeometry heating geometry representation
 * @param heatingCapacity heating capacity specification
 * @param heatingElements heating element features
 * @param operatingTemperature operating temperature range
 * @param heatingControl heating control specification
 */
public record StepHeatingFeature(
    int id,
    String name,
    String heatingType,
    StepEntity heatingGeometry,
    double heatingCapacity,
    List<StepEntity> heatingElements,
    List<Double> operatingTemperature,
    StepEntity heatingControl) implements StepEntity {
}