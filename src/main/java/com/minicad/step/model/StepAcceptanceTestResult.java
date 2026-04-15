package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ACCEPTANCE_TEST_RESULT.
 * An acceptance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceCriteria acceptance variance criteria
 * @varianceTests acceptance variance test cases
 * @varianceOutcome acceptance variance outcome
 * @varianceSignoff signoff variance reference
 * @varianceStatus result variance status
 */
public record StepAcceptanceTestResult(
    int id,
    String name,
    StepEntity varianceSystem,
    StepEntity varianceCriteria,
    List<StepEntity> varianceTests,
    String varianceOutcome,
    StepEntity varianceSignoff,
    String varianceStatus) implements StepEntity {
}