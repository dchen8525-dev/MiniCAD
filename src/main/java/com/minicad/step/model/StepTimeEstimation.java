package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TIME_ESTIMATION.
 * A time estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (setup, operation, total)
 * @param estimatedTime estimated time value
 * @param timeUnit time unit specification
 * @param timeBreakdown time breakdown items
 * @param estimationMethod estimation method used
 * @param estimationFactors estimation factors applied
 */
public final class StepTimeEstimation extends AbstractStepEntity {
    private final String estimationType;
    private final double estimatedTime;
    private final String timeUnit;
    private final List<StepEntity> timeBreakdown;
    private final String estimationMethod;
    private final List<Double> estimationFactors;

    public StepTimeEstimation(int id, String name, String estimationType, double estimatedTime, String timeUnit, List<StepEntity> timeBreakdown, String estimationMethod, List<Double> estimationFactors) {
        super(id, name);
        this.estimationType = estimationType;
        this.estimatedTime = estimatedTime;
        this.timeUnit = timeUnit;
        this.timeBreakdown = timeBreakdown == null ? null : java.util.List.copyOf(timeBreakdown);
        this.estimationMethod = estimationMethod;
        this.estimationFactors = estimationFactors == null ? null : java.util.List.copyOf(estimationFactors);
    }

    public String getEstimationType() {
        return estimationType;
    }

    public double getEstimatedTime() {
        return estimatedTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public List<StepEntity> getTimeBreakdown() {
        return timeBreakdown;
    }

    public String getEstimationMethod() {
        return estimationMethod;
    }

    public List<Double> getEstimationFactors() {
        return estimationFactors;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("estimationType", estimationType);
        state.put("estimatedTime", estimatedTime);
        state.put("timeUnit", timeUnit);
        state.put("timeBreakdown", timeBreakdown);
        state.put("estimationMethod", estimationMethod);
        state.put("estimationFactors", estimationFactors);
        return state;
    }
}
