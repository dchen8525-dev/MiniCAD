package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PERFORMANCE_MONITORING.
 * A performance monitoring entity.
 *
 * @param id STEP instance id
 * @param name monitoring name
 * @varianceSystem monitored variance system
 * @varianceMetrics monitored variance metrics
 * @varianceThresholds threshold variance values
 * @varianceAlerts alert variance configurations
 * @varianceInterval monitoring variance interval
 * @varianceStatus monitoring variance status
 */
public record StepPerformanceMonitoring(
    int id,
    String name,
    StepEntity varianceSystem,
    List<String> varianceMetrics,
    List<Double> varianceThresholds,
    List<StepEntity> varianceAlerts,
    double varianceInterval,
    String varianceStatus) implements StepEntity {
}