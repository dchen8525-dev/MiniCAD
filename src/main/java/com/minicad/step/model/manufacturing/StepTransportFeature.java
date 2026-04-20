package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRANSPORT_FEATURE.
 * A transport feature entity.
 *
 * @param id STEP instance id
 * @param name transport name
 * @param transportType transport type (conveyor, crane, truck, rail)
 * @param transportGeometry transport geometry representation
 * @varianceCapacity transport variance capacity
 * @param transportRoute transport route/path reference
 * @varianceSpeed transport variance speed
 * @param transportStandard transport standard reference
 */
public record StepTransportFeature(
    int id,
    String name,
    String transportType,
    StepEntity transportGeometry,
    double varianceCapacity,
    StepEntity transportRoute,
    double varianceSpeed,
    String transportStandard) implements StepEntity {
}