package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FAULT_RECORD.
 * A fault record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem faulty variance system
 * @varianceFault fault variance description
 * @varianceCode fault variance code
 * @varianceDate fault variance date
 * @varianceDiagnosis diagnosis variance result
 * @varianceRemedy remedy variance action
 * @varianceStatus record variance status
 */
public record StepFaultRecord(
    int id,
    String name,
    StepEntity varianceSystem,
    String varianceFault,
    String varianceCode,
    StepEntity varianceDate,
    String varianceDiagnosis,
    String varianceRemedy,
    String varianceStatus) implements StepEntity {
}