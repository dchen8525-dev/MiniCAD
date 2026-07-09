package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved METRIC_INSTANCE.
 * A metric instance entity.
 *
 * @param id STEP instance id
 * @param name metric instance name
 * @param metricDefinition metric variance definition reference
 * @param metricValue metric variance current value
 * @param metricTrend metric variance trend direction
 * @param metricHistory metric variance historical values
 * @param metricStatus metric variance status
 */
/**
 * Resolved METRIC_INSTANCE.
 * A metric instance entity.
 *
 * @param id STEP instance id
 * @param name metric instance name
 * @param metricDefinition metric variance definition reference
 * @param metricValue metric variance current value
 * @param metricTrend metric variance trend direction
 * @param metricHistory metric variance historical values
 * @param metricStatus metric variance status
 */
public final class StepMetricInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity metricDefinition;
    private final double metricValue;
    private final String metricTrend;
    private final List<Double> metricHistory;
    private final String metricStatus;

    public StepMetricInstance(int id, String name, StepEntity metricDefinition, double metricValue, String metricTrend, List<Double> metricHistory, String metricStatus) {
        this.id = id;
        this.name = name;
        this.metricDefinition = metricDefinition;
        this.metricValue = metricValue;
        this.metricTrend = metricTrend;
        this.metricHistory = metricHistory == null ? null : java.util.List.copyOf(metricHistory);
        this.metricStatus = metricStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMetricDefinition() {
        return metricDefinition;
    }

    public double getMetricValue() {
        return metricValue;
    }

    public String getMetricTrend() {
        return metricTrend;
    }

    public List<Double> getMetricHistory() {
        return metricHistory;
    }

    public String getMetricStatus() {
        return metricStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMetricInstance that = (StepMetricInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(metricDefinition, that.metricDefinition) && metricValue == that.metricValue && Objects.equals(metricTrend, that.metricTrend) && Objects.equals(metricHistory, that.metricHistory) && Objects.equals(metricStatus, that.metricStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, metricDefinition, metricValue, metricTrend, metricHistory, metricStatus);
    }

    @Override
    public String toString() {
        return "StepMetricInstance{" + "id=" + id + "name=" + name + "metricDefinition=" + metricDefinition + "metricValue=" + metricValue + "metricTrend=" + metricTrend + "metricHistory=" + metricHistory + "metricStatus=" + metricStatus + "}";
    }
}