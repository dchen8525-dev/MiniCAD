package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPLIANCE_RECORD.
 * A compliance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem compliance variance item
 * @varianceStandard compliance variance standard
 * @varianceRequirements compliance variance requirements
 * @varianceEvidence compliance variance evidence
 * @varianceDate compliance variance date
 * @varianceStatus record variance status
 */
public record StepComplianceRecord(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceStandard,
    List<String> varianceRequirements,
    List<StepEntity> varianceEvidence,
    StepEntity varianceDate,
    String varianceStatus) implements StepEntity {
}