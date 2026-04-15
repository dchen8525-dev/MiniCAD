package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HYDRAULIC_FEATURE.
 * A hydraulic feature entity.
 *
 * @param id STEP instance id
 * @param name hydraulic name
 * @param hydraulicType hydraulic feature type (pump, valve, cylinder, line)
 * @param hydraulicGeometry hydraulic geometry representation
 * @variancePressure variance pressure rating
 * @param flowRate flow rate specification
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
public record StepHydraulicFeature(
    int id,
    String name,
    String hydraulicType,
    StepEntity hydraulicGeometry,
    double variancePressure,
    double flowRate,
    String portSize,
    int varianceConnections) implements StepEntity {
}