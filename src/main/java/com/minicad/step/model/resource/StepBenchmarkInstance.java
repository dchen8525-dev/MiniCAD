package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BENCHMARK_INSTANCE.
 * A benchmark instance entity.
 *
 * @param id STEP instance id
 * @param name benchmark instance name
 * @param benchmarkDefinition benchmark variance definition reference
 * @param benchmarkValues benchmark variance measured values
 * @param benchmarkScore benchmark variance score/rating
 * @param benchmarkStatus benchmark variance status
 */
/**
 * Resolved BENCHMARK_INSTANCE.
 * A benchmark instance entity.
 *
 * @param id STEP instance id
 * @param name benchmark instance name
 * @param benchmarkDefinition benchmark variance definition reference
 * @param benchmarkValues benchmark variance measured values
 * @param benchmarkScore benchmark variance score/rating
 * @param benchmarkStatus benchmark variance status
 */
public final class StepBenchmarkInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity benchmarkDefinition;
    private final List<Double> benchmarkValues;
    private final double benchmarkScore;
    private final String benchmarkStatus;

    public StepBenchmarkInstance(int id, String name, StepEntity benchmarkDefinition, List<Double> benchmarkValues, double benchmarkScore, String benchmarkStatus) {
        this.id = id;
        this.name = name;
        this.benchmarkDefinition = benchmarkDefinition;
        this.benchmarkValues = benchmarkValues == null ? null : java.util.List.copyOf(benchmarkValues);
        this.benchmarkScore = benchmarkScore;
        this.benchmarkStatus = benchmarkStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBenchmarkDefinition() {
        return benchmarkDefinition;
    }

    public List<Double> getBenchmarkValues() {
        return benchmarkValues;
    }

    public double getBenchmarkScore() {
        return benchmarkScore;
    }

    public String getBenchmarkStatus() {
        return benchmarkStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBenchmarkInstance that = (StepBenchmarkInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(benchmarkDefinition, that.benchmarkDefinition) && Objects.equals(benchmarkValues, that.benchmarkValues) && benchmarkScore == that.benchmarkScore && Objects.equals(benchmarkStatus, that.benchmarkStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, benchmarkDefinition, benchmarkValues, benchmarkScore, benchmarkStatus);
    }

    @Override
    public String toString() {
        return "StepBenchmarkInstance{" + "id=" + id + "name=" + name + "benchmarkDefinition=" + benchmarkDefinition + "benchmarkValues=" + benchmarkValues + "benchmarkScore=" + benchmarkScore + "benchmarkStatus=" + benchmarkStatus + "}";
    }
}