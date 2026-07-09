package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPerformanceTestResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final List<String> varianceMetrics;
    private final List<Double> varianceValues;
    private final StepEntity varianceBaseline;
    private final boolean varianceCondition;
    private final String varianceStatus;

    public StepPerformanceTestResult(int id, String name, StepEntity varianceSystem, List<String> varianceMetrics, List<Double> varianceValues, StepEntity varianceBaseline, boolean varianceCondition, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceValues = varianceValues == null ? null : java.util.List.copyOf(varianceValues);
        this.varianceBaseline = varianceBaseline;
        this.varianceCondition = varianceCondition;
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

    public List<Double> getVarianceValues() {
        return varianceValues;
    }

    public StepEntity getVarianceBaseline() {
        return varianceBaseline;
    }

    public boolean isVarianceCondition() {
        return varianceCondition;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPerformanceTestResult that = (StepPerformanceTestResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceMetrics, that.varianceMetrics) && Objects.equals(varianceValues, that.varianceValues) && Objects.equals(varianceBaseline, that.varianceBaseline) && varianceCondition == that.varianceCondition && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceMetrics, varianceValues, varianceBaseline, varianceCondition, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepPerformanceTestResult{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceMetrics=" + varianceMetrics + "varianceValues=" + varianceValues + "varianceBaseline=" + varianceBaseline + "varianceCondition=" + varianceCondition + "varianceStatus=" + varianceStatus + "}";
    }
}