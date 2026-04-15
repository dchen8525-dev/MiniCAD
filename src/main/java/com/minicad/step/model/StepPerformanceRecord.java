package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PERFORMANCE_RECORD.
 * A performance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson evaluated variance person
 * @variancePeriod evaluation variance period
 * @varianceMetrics performance variance metrics
 * @varianceScores performance variance scores
 * @varianceGoals performance variance goals
 * @varianceStatus record variance status
 */
public record StepPerformanceRecord(
    int id,
    String name,
    StepEntity variancePerson,
    String variancePeriod,
    List<String> varianceMetrics,
    List<Double> varianceScores,
    List<String> varianceGoals,
    String varianceStatus) implements StepEntity {
}