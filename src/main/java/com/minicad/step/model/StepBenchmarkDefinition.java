package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BENCHMARK_DEFINITION.
 * A benchmark definition entity.
 *
 * @param id STEP instance id
 * @param name benchmark name
 * @param benchmarkType benchmark variance type
 * @param benchmarkMetrics benchmark variance metrics
 * @param benchmarkReference benchmark variance reference values
 * @param benchmarkDescription benchmark variance description
 * @param benchmarkStatus benchmark variance status
 */
/**
 * Resolved BENCHMARK_DEFINITION.
 * A benchmark definition entity.
 *
 * @param id STEP instance id
 * @param name benchmark name
 * @param benchmarkType benchmark variance type
 * @param benchmarkMetrics benchmark variance metrics
 * @param benchmarkReference benchmark variance reference values
 * @param benchmarkDescription benchmark variance description
 * @param benchmarkStatus benchmark variance status
 */
public final class StepBenchmarkDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String benchmarkType;
    private final List<StepEntity> benchmarkMetrics;
    private final List<Double> benchmarkReference;
    private final String benchmarkDescription;
    private final String benchmarkStatus;

    public StepBenchmarkDefinition(int id, String name, String benchmarkType, List<StepEntity> benchmarkMetrics, List<Double> benchmarkReference, String benchmarkDescription, String benchmarkStatus) {
        this.id = id;
        this.name = name;
        this.benchmarkType = benchmarkType;
        this.benchmarkMetrics = benchmarkMetrics == null ? null : java.util.List.copyOf(benchmarkMetrics);
        this.benchmarkReference = benchmarkReference == null ? null : java.util.List.copyOf(benchmarkReference);
        this.benchmarkDescription = benchmarkDescription;
        this.benchmarkStatus = benchmarkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBenchmarkType() {
        return benchmarkType;
    }

    public List<StepEntity> getBenchmarkMetrics() {
        return benchmarkMetrics;
    }

    public List<Double> getBenchmarkReference() {
        return benchmarkReference;
    }

    public String getBenchmarkDescription() {
        return benchmarkDescription;
    }

    public String getBenchmarkStatus() {
        return benchmarkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBenchmarkDefinition that = (StepBenchmarkDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(benchmarkType, that.benchmarkType) && Objects.equals(benchmarkMetrics, that.benchmarkMetrics) && Objects.equals(benchmarkReference, that.benchmarkReference) && Objects.equals(benchmarkDescription, that.benchmarkDescription) && Objects.equals(benchmarkStatus, that.benchmarkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, benchmarkType, benchmarkMetrics, benchmarkReference, benchmarkDescription, benchmarkStatus);
    }

    @Override
    public String toString() {
        return "StepBenchmarkDefinition{" + "id=" + id + "name=" + name + "benchmarkType=" + benchmarkType + "benchmarkMetrics=" + benchmarkMetrics + "benchmarkReference=" + benchmarkReference + "benchmarkDescription=" + benchmarkDescription + "benchmarkStatus=" + benchmarkStatus + "}";
    }
}