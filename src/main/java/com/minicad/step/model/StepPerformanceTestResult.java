package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PERFORMANCE_TEST_RESULT.
 * A performance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceMetrics performance variance metrics
 * @varianceValues measured variance values
 * @varianceBaseline baseline variance reference
 * @varianceCondition condition variance met flag
 * @varianceStatus result variance status
 */
public record StepPerformanceTestResult(
    int id,
    String name,
    StepEntity varianceSystem,
    List<String> varianceMetrics,
    List<Double> varianceValues,
    StepEntity varianceBaseline,
    boolean varianceCondition,
    String varianceStatus) implements StepEntity {
}