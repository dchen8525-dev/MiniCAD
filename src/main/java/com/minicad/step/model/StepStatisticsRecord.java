package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepStatisticsRecord extends AbstractStepEntity {
    private final String varianceSubject;
    private final List<String> varianceMetrics;
    private final List<Double> varianceValues;
    private final String variancePeriod;
    private final String varianceTrend;
    private final String varianceStatus;

    public StepStatisticsRecord(int id, String name, String varianceSubject, List<String> varianceMetrics, List<Double> varianceValues, String variancePeriod, String varianceTrend, String varianceStatus) {
        super(id, name);
        this.varianceSubject = varianceSubject;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceValues = varianceValues == null ? null : java.util.List.copyOf(varianceValues);
        this.variancePeriod = variancePeriod;
        this.varianceTrend = varianceTrend;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceSubject", varianceSubject,
                "varianceMetrics", varianceMetrics, "varianceValues", varianceValues,
                "variancePeriod", variancePeriod, "varianceTrend", varianceTrend,
                "varianceStatus", varianceStatus);
    }
}
