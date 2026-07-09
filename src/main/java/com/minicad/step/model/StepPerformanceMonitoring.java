package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPerformanceMonitoring implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final List<String> varianceMetrics;
    private final List<Double> varianceThresholds;
    private final List<StepEntity> varianceAlerts;
    private final double varianceInterval;
    private final String varianceStatus;

    public StepPerformanceMonitoring(int id, String name, StepEntity varianceSystem, List<String> varianceMetrics, List<Double> varianceThresholds, List<StepEntity> varianceAlerts, double varianceInterval, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceThresholds = varianceThresholds == null ? null : java.util.List.copyOf(varianceThresholds);
        this.varianceAlerts = varianceAlerts == null ? null : java.util.List.copyOf(varianceAlerts);
        this.varianceInterval = varianceInterval;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPerformanceMonitoring that = (StepPerformanceMonitoring) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceMetrics, that.varianceMetrics) && Objects.equals(varianceThresholds, that.varianceThresholds) && Objects.equals(varianceAlerts, that.varianceAlerts) && varianceInterval == that.varianceInterval && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceMetrics, varianceThresholds, varianceAlerts, varianceInterval, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepPerformanceMonitoring{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceMetrics=" + varianceMetrics + "varianceThresholds=" + varianceThresholds + "varianceAlerts=" + varianceAlerts + "varianceInterval=" + varianceInterval + "varianceStatus=" + varianceStatus + "}";
    }
}