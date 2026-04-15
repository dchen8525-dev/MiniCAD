package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DECOMMISSION_RECORD.
 * A decommission record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment decommissioned variance equipment
 * @varianceReason decommission variance reason
 * @varianceDate decommission variance date
 * @varianceDisposition disposition variance action
 * @varianceDocumentation documentation variance reference
 * @varianceStatus record variance status
 */
public record StepDecommissionRecord(
    int id,
    String name,
    StepEntity varianceEquipment,
    String varianceReason,
    StepEntity varianceDate,
    String varianceDisposition,
    StepEntity varianceDocumentation,
    String varianceStatus) implements StepEntity {
}