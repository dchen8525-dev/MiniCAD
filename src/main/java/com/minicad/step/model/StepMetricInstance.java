package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMetricInstance extends AbstractStepEntity {
    private final StepEntity metricDefinition;
    private final double metricValue;
    private final String metricTrend;
    private final List<Double> metricHistory;
    private final String metricStatus;

    public StepMetricInstance(int id, String name, StepEntity metricDefinition, double metricValue, String metricTrend, List<Double> metricHistory, String metricStatus) {
        super(id, name);
        this.metricDefinition = metricDefinition;
        this.metricValue = metricValue;
        this.metricTrend = metricTrend;
        this.metricHistory = metricHistory == null ? null : java.util.List.copyOf(metricHistory);
        this.metricStatus = metricStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("metricDefinition", metricDefinition);
        state.put("metricValue", metricValue);
        state.put("metricTrend", metricTrend);
        state.put("metricHistory", metricHistory);
        state.put("metricStatus", metricStatus);
        return state;
    }
}
