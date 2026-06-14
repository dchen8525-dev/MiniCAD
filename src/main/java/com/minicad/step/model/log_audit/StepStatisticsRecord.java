package com.minicad.step.model.log_audit;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STATISTICS_RECORD.
 * A statistics record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSubject statistics variance subject
 * @varianceMetrics statistical variance metrics
 * @varianceValues statistical variance values
 * @variancePeriod statistics variance period
 * @varianceTrend trend variance analysis
 * @varianceStatus record variance status
 */
/**
 * Resolved STATISTICS_RECORD.
 * A statistics record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSubject statistics variance subject
 * @varianceMetrics statistical variance metrics
 * @varianceValues statistical variance values
 * @variancePeriod statistics variance period
 * @varianceTrend trend variance analysis
 * @varianceStatus record variance status
 */
public final class StepStatisticsRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceSubject;
    private final List<String> varianceMetrics;
    private final List<Double> varianceValues;
    private final String variancePeriod;
    private final String varianceTrend;
    private final String varianceStatus;

    public StepStatisticsRecord(int id, String name, String varianceSubject, List<String> varianceMetrics, List<Double> varianceValues, String variancePeriod, String varianceTrend, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSubject = varianceSubject;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceValues = varianceValues == null ? null : java.util.List.copyOf(varianceValues);
        this.variancePeriod = variancePeriod;
        this.varianceTrend = varianceTrend;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceSubject() {
        return varianceSubject;
    }

    public List<String> getVarianceMetrics() {
        return varianceMetrics;
    }

    public List<Double> getVarianceValues() {
        return varianceValues;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public String getVarianceTrend() {
        return varianceTrend;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStatisticsRecord that = (StepStatisticsRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSubject, that.varianceSubject) && Objects.equals(varianceMetrics, that.varianceMetrics) && Objects.equals(varianceValues, that.varianceValues) && Objects.equals(variancePeriod, that.variancePeriod) && Objects.equals(varianceTrend, that.varianceTrend) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSubject, varianceMetrics, varianceValues, variancePeriod, varianceTrend, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepStatisticsRecord{" + "id=" + id + "name=" + name + "varianceSubject=" + varianceSubject + "varianceMetrics=" + varianceMetrics + "varianceValues=" + varianceValues + "variancePeriod=" + variancePeriod + "varianceTrend=" + varianceTrend + "varianceStatus=" + varianceStatus + "}";
    }
}