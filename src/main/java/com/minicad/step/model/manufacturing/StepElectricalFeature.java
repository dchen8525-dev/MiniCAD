package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ELECTRICAL_FEATURE.
 * An electrical feature entity.
 *
 * @param id STEP instance id
 * @param name electrical name
 * @param electricalType electrical feature type (connector, wire, terminal)
 * @param electricalGeometry electrical geometry representation
 * @param voltageRating voltage rating specification
 * @param currentRating current rating specification
 * @param wireGauge wire gauge specification
 * @variancePins variance pins count for connectors
 */
public record StepElectricalFeature(
    int id,
    String name,
    String electricalType,
    StepEntity electricalGeometry,
    double voltageRating,
    double currentRating,
    String wireGauge,
    int variancePins) implements StepEntity {
}