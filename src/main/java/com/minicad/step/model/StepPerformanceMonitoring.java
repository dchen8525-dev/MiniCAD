package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPerformanceMonitoring extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final List<String> varianceMetrics;
    private final List<Double> varianceThresholds;
    private final List<StepEntity> varianceAlerts;
    private final double varianceInterval;
    private final String varianceStatus;

    public StepPerformanceMonitoring(int id, String name, StepEntity varianceSystem, List<String> varianceMetrics, List<Double> varianceThresholds, List<StepEntity> varianceAlerts, double varianceInterval, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceThresholds = varianceThresholds == null ? null : java.util.List.copyOf(varianceThresholds);
        this.varianceAlerts = varianceAlerts == null ? null : java.util.List.copyOf(varianceAlerts);
        this.varianceInterval = varianceInterval;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public List<String> getVarianceMetrics() {
        return varianceMetrics;
    }

    public List<Double> getVarianceThresholds() {
        return varianceThresholds;
    }

    public List<StepEntity> getVarianceAlerts() {
        return varianceAlerts;
    }

    public double getVarianceInterval() {
        return varianceInterval;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceMetrics", varianceMetrics);
        state.put("varianceThresholds", varianceThresholds);
        state.put("varianceAlerts", varianceAlerts);
        state.put("varianceInterval", varianceInterval);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
