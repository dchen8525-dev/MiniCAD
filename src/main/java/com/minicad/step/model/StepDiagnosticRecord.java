package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DIAGNOSTIC_RECORD.
 * A diagnostic record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem diagnosed variance system
 * @varianceTests diagnostic variance tests
 * @varianceResults diagnostic variance results
 * @varianceConclusion diagnostic variance conclusion
 * @varianceRecommendation recommendation variance for fix
 * @varianceStatus record variance status
 */
public record StepDiagnosticRecord(
    int id,
    String name,
    StepEntity varianceSystem,
    List<String> varianceTests,
    List<String> varianceResults,
    String varianceConclusion,
    String varianceRecommendation,
    String varianceStatus) implements StepEntity {
}