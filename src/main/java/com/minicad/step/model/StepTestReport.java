package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TEST_REPORT.
 * A test report entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportId report identifier
 * @param testType test type (functional, performance, environmental)
 * @varianceResults test variance results
 * @varianceConclusions test variance conclusions
 * @varianceRecommendations test variance recommendations
 * @varianceDate test variance date
 * @param reportStatus report status
 */
public record StepTestReport(
    int id,
    String name,
    String reportId,
    String testType,
    List<StepEntity> varianceResults,
    String varianceConclusions,
    List<String> varianceRecommendations,
    StepEntity varianceDate,
    String reportStatus) implements StepEntity {
}