package com.minicad.step.model.system;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSystemHealth implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final String varianceStatus;
    private final List<String> varianceIssues;
    private final List<Double> varianceMetrics;
    private final StepEntity varianceLastCheck;
    private final String varianceStatus2;

    public StepSystemHealth(int id, String name, StepEntity varianceSystem, String varianceStatus, List<String> varianceIssues, List<Double> varianceMetrics, StepEntity varianceLastCheck, String varianceStatus2) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceStatus = varianceStatus;
        this.varianceIssues = varianceIssues == null ? null : java.util.List.copyOf(varianceIssues);
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceLastCheck = varianceLastCheck;
        this.varianceStatus2 = varianceStatus2;
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

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public List<String> getVarianceIssues() {
        return varianceIssues;
    }

    public List<Double> getVarianceMetrics() {
        return varianceMetrics;
    }

    public StepEntity getVarianceLastCheck() {
        return varianceLastCheck;
    }

    public String getVarianceStatus2() {
        return varianceStatus2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSystemHealth that = (StepSystemHealth) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(varianceIssues, that.varianceIssues) && Objects.equals(varianceMetrics, that.varianceMetrics) && Objects.equals(varianceLastCheck, that.varianceLastCheck) && Objects.equals(varianceStatus2, that.varianceStatus2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceStatus, varianceIssues, varianceMetrics, varianceLastCheck, varianceStatus2);
    }

    @Override
    public String toString() {
        return "StepSystemHealth{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceStatus=" + varianceStatus + "varianceIssues=" + varianceIssues + "varianceMetrics=" + varianceMetrics + "varianceLastCheck=" + varianceLastCheck + "varianceStatus2=" + varianceStatus2 + "}";
    }
}