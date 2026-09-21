package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPerformanceRecord extends AbstractStepEntity {
    private final StepEntity variancePerson;
    private final String variancePeriod;
    private final List<String> varianceMetrics;
    private final List<Double> varianceScores;
    private final List<String> varianceGoals;
    private final String varianceStatus;

    public StepPerformanceRecord(int id, String name, StepEntity variancePerson, String variancePeriod, List<String> varianceMetrics, List<Double> varianceScores, List<String> varianceGoals, String varianceStatus) {
        super(id, name);
        this.variancePerson = variancePerson;
        this.variancePeriod = variancePeriod;
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceScores = varianceScores == null ? null : java.util.List.copyOf(varianceScores);
        this.varianceGoals = varianceGoals == null ? null : java.util.List.copyOf(varianceGoals);
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("variancePerson", variancePerson);
        state.put("variancePeriod", variancePeriod);
        state.put("varianceMetrics", varianceMetrics);
        state.put("varianceScores", varianceScores);
        state.put("varianceGoals", varianceGoals);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
