package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SYSTEM_HEALTH.
 * A system health entity.
 *
 * @param id STEP instance id
 * @param name health name
 * @varianceSystem system variance reference
 * @varianceStatus health variance status (healthy, degraded, critical)
 * @varianceIssues health variance issues detected
 * @varianceMetrics health variance metrics
 * @varianceLastCheck last variance check date
 * @varianceStatus2 record variance status
 */
public record StepSystemHealth(
    int id,
    String name,
    StepEntity varianceSystem,
    String varianceStatus,
    List<String> varianceIssues,
    List<Double> varianceMetrics,
    StepEntity varianceLastCheck,
    String varianceStatus2) implements StepEntity {
}