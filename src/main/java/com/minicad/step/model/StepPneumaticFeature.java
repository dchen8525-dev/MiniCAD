package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PNEUMATIC_FEATURE.
 * A pneumatic feature entity.
 *
 * @param id STEP instance id
 * @param name pneumatic name
 * @param pneumaticType pneumatic feature type (compressor, valve, cylinder, line)
 * @param pneumaticGeometry pneumatic geometry representation
 * @param variancePressure variance pressure rating
 * @varianceFlow variance flow rate
 * @param portSize port size specification
 * @varianceConnections variance connections count
 */
public record StepPneumaticFeature(
    int id,
    String name,
    String pneumaticType,
    StepEntity pneumaticGeometry,
    double variancePressure,
    double varianceFlow,
    String portSize,
    int varianceConnections) implements StepEntity {
}