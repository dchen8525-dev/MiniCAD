package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TROUBLESHOOTING_RECORD.
 * A troubleshooting record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceProblem problem variance description
 * @varianceSymptoms symptoms variance observed
 * @varianceSteps troubleshooting variance steps taken
 * @varianceSolution solution variance found
 * @varianceTime time variance to resolve
 * @varianceStatus record variance status
 */
public record StepTroubleshootingRecord(
    int id,
    String name,
    String varianceProblem,
    List<String> varianceSymptoms,
    List<String> varianceSteps,
    String varianceSolution,
    double varianceTime,
    String varianceStatus) implements StepEntity {
}