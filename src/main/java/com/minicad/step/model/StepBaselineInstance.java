package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BASELINE_INSTANCE.
 * A baseline instance entity.
 *
 * @param id STEP instance id
 * @param name baseline instance name
 * @param baselineDefinition baseline variance definition reference
 * @param baselineActualValues baseline variance actual values
 * @param baselineVariance baseline variance deviation from baseline
 * @param baselineStatus baseline variance status
 */
/**
 * Resolved BASELINE_INSTANCE.
 * A baseline instance entity.
 *
 * @param id STEP instance id
 * @param name baseline instance name
 * @param baselineDefinition baseline variance definition reference
 * @param baselineActualValues baseline variance actual values
 * @param baselineVariance baseline variance deviation from baseline
 * @param baselineStatus baseline variance status
 */
public final class StepBaselineInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity baselineDefinition;
    private final List<Double> baselineActualValues;
    private final double baselineVariance;
    private final String baselineStatus;

    public StepBaselineInstance(int id, String name, StepEntity baselineDefinition, List<Double> baselineActualValues, double baselineVariance, String baselineStatus) {
        this.id = id;
        this.name = name;
        this.baselineDefinition = baselineDefinition;
        this.baselineActualValues = baselineActualValues == null ? null : java.util.List.copyOf(baselineActualValues);
        this.baselineVariance = baselineVariance;
        this.baselineStatus = baselineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBaselineDefinition() {
        return baselineDefinition;
    }

    public List<Double> getBaselineActualValues() {
        return baselineActualValues;
    }

    public double getBaselineVariance() {
        return baselineVariance;
    }

    public String getBaselineStatus() {
        return baselineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBaselineInstance that = (StepBaselineInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baselineDefinition, that.baselineDefinition) && Objects.equals(baselineActualValues, that.baselineActualValues) && baselineVariance == that.baselineVariance && Objects.equals(baselineStatus, that.baselineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baselineDefinition, baselineActualValues, baselineVariance, baselineStatus);
    }

    @Override
    public String toString() {
        return "StepBaselineInstance{" + "id=" + id + "name=" + name + "baselineDefinition=" + baselineDefinition + "baselineActualValues=" + baselineActualValues + "baselineVariance=" + baselineVariance + "baselineStatus=" + baselineStatus + "}";
    }
}