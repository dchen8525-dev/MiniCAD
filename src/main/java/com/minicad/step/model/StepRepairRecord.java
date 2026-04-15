package com.minicad.step.model;

import java.util.List;

/**
 * Resolved REPAIR_RECORD.
 * A repair record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment repaired variance equipment
 * @varianceProblem repair variance problem description
 * @varianceCause repair variance root cause
 * @varianceDate repair variance date
 * @varianceActions repair variance actions
 * @varianceStatus record variance status
 */
public record StepRepairRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    String varianceProblem,
    String varianceCause,
    StepEntity varianceDate,
    List<String> varianceActions,
    String varianceStatus) implements StepEntity {
}