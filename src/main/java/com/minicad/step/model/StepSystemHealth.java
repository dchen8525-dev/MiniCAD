package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SYSTEM_HEALTH.
 * A system health entity.
 *
 * @param id STEP instance id
 * @param name health name
 * @varianceSystem system variance reference
 * @varianceStatus health variance status (healthy, degraded, critical)
 * @varianceIssues health variance issues detected
 * @varianceMetrics health variance metrics
 * @varianceLastCheck last variance check date
 * @varianceStatus2 record variance status
 */
public final class StepSystemHealth extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final String varianceStatus;
    private final List<String> varianceIssues;
    private final List<Double> varianceMetrics;
    private final StepEntity varianceLastCheck;
    private final String varianceStatus2;

    public StepSystemHealth(int id, String name, StepEntity varianceSystem, String varianceStatus, List<String> varianceIssues, List<Double> varianceMetrics, StepEntity varianceLastCheck, String varianceStatus2) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceStatus = varianceStatus;
        this.varianceIssues = varianceIssues == null ? null : java.util.List.copyOf(varianceIssues);
        this.varianceMetrics = varianceMetrics == null ? null : java.util.List.copyOf(varianceMetrics);
        this.varianceLastCheck = varianceLastCheck;
        this.varianceStatus2 = varianceStatus2;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public List<String> getVarianceIssues() {
        return varianceIssues;
    }

    public List<Double> getVarianceMetrics() {
        return varianceMetrics;
    }

    public StepEntity getVarianceLastCheck() {
        return varianceLastCheck;
    }

    public String getVarianceStatus2() {
        return varianceStatus2;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceStatus", varianceStatus);
        state.put("varianceIssues", varianceIssues);
        state.put("varianceMetrics", varianceMetrics);
        state.put("varianceLastCheck", varianceLastCheck);
        state.put("varianceStatus2", varianceStatus2);
        return state;
    }
}
