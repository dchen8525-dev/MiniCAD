package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PERFORMANCE_RECORD.
 * A performance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson evaluated variance person
 * @variancePeriod evaluation variance period
 * @varianceMetrics performance variance metrics
 * @varianceScores performance variance scores
 * @varianceGoals performance variance goals
 * @varianceStatus record variance status
 */
/**
 * Resolved PERFORMANCE_RECORD.
 * A performance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @variancePerson evaluated variance person
 * @variancePeriod evaluation variance period
 * @varianceMetrics performance variance metrics
 * @varianceScores performance variance scores
 * @varianceGoals performance variance goals
 * @varianceStatus record variance status
 */
public final class StepPerformanceRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity variancePerson;
    private final String variancePeriod;
    private final List<String> varianceMetrics;
    private final List<Double> varianceScores;
    private final List<String> varianceGoals;
    private final String varianceStatus;

    public StepPerformanceRecord(int id, String name, StepEntity variancePerson, String variancePeriod, List<String> varianceMetrics, List<Double> varianceScores, List<String> varianceGoals, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.variancePerson = variancePerson;
        this.variancePeriod = variancePeriod;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceScores = varianceScores == null ? null : java.util.List.copyOf(varianceScores);
        this.varianceGoals = varianceGoals == null ? null : java.util.List.copyOf(varianceGoals);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVariancePerson() {
        return variancePerson;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public List<String> getVarianceMetrics() {
        return varianceMetrics;
    }

    public List<Double> getVarianceScores() {
        return varianceScores;
    }

    public List<String> getVarianceGoals() {
        return varianceGoals;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPerformanceRecord that = (StepPerformanceRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variancePerson, that.variancePerson) && Objects.equals(variancePeriod, that.variancePeriod) && Objects.equals(varianceMetrics, that.varianceMetrics) && Objects.equals(varianceScores, that.varianceScores) && Objects.equals(varianceGoals, that.varianceGoals) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variancePerson, variancePeriod, varianceMetrics, varianceScores, varianceGoals, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepPerformanceRecord{" + "id=" + id + "name=" + name + "variancePerson=" + variancePerson + "variancePeriod=" + variancePeriod + "varianceMetrics=" + varianceMetrics + "varianceScores=" + varianceScores + "varianceGoals=" + varianceGoals + "varianceStatus=" + varianceStatus + "}";
    }
}