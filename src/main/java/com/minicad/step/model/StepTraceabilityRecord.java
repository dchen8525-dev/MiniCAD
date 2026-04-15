package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRACEABILITY_RECORD.
 * A traceability record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem traced variance item
 * @varianceOrigin origin variance source
 * @variancePath trace variance path/chain
 * @varianceDestination destination variance reference
 * @varianceDate trace variance date
 * @varianceStatus record variance status
 */
public record StepTraceabilityRecord(
    int id,
    String name,
    StepEntity varianceItem,
    StepEntity varianceOrigin,
    List<StepEntity> variancePath,
    StepEntity varianceDestination,
    StepEntity varianceDate,
    String varianceStatus) implements StepEntity {
}