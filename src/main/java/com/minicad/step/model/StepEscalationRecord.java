package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ESCALATION_RECORD.
 * An escalation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceIssue escalated variance issue
 * @varianceFrom escalation variance from level
 * @varianceTo escalation variance to level
 * @varianceReason escalation variance reason
 * @varianceDate escalation variance date
 * @varianceHandler handler variance reference
 * @varianceStatus record variance status
 */
public record StepEscalationRecord(
    int id,
    String name,
    StepEntity varianceIssue,
    int varianceFrom,
    int varianceTo,
    String varianceReason,
    StepEntity varianceDate,
    StepEntity varianceHandler,
    String varianceStatus) implements StepEntity {
}