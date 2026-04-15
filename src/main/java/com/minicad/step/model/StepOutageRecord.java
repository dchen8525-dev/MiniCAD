package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OUTAGE_RECORD.
 * An outage record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceCause outage variance cause
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceDuration outage variance duration
 * @varianceImpact impact variance description
 * @varianceStatus record variance status
 */
public record StepOutageRecord(
    int id,
    String name,
    StepEntity varianceSystem,
    String varianceCause,
    StepEntity varianceStartTime,
    StepEntity varianceEndTime,
    double varianceDuration,
    String varianceImpact,
    String varianceStatus) implements StepEntity {
}