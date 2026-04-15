package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VERIFICATION_RESULT.
 * A verification result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem verified variance item
 * @varianceMethod verification variance method
 * @varianceCriteria verification variance criteria
 * @varianceOutcome verification variance outcome (pass/fail)
 * @varianceEvidence evidence variance reference
 * @varianceStatus result variance status
 */
public record StepVerificationResult(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceMethod,
    StepEntity varianceCriteria,
    String varianceOutcome,
    StepEntity varianceEvidence,
    String varianceStatus) implements StepEntity {
}