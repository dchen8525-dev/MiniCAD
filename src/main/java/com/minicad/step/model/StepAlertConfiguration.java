package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ALERT_CONFIGURATION.
 * An alert configuration entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @varianceCondition alert variance condition
 * @varianceThreshold threshold variance value
 * @varianceActions alert variance actions
 * @varianceRecipients alert variance recipients
 * @varianceSeverity alert variance severity level
 * @varianceStatus configuration variance status
 */
public final class StepAlertConfiguration extends AbstractStepEntity {
    private final String varianceCondition;
    private final double varianceThreshold;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceRecipients;
    private final int varianceSeverity;
    private final String varianceStatus;

    public StepAlertConfiguration(int id, String name, String varianceCondition, double varianceThreshold, List<StepEntity> varianceActions, List<StepEntity> varianceRecipients, int varianceSeverity, String varianceStatus) {
        super(id, name);
        this.varianceCondition = varianceCondition;
        this.varianceThreshold = varianceThreshold;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceRecipients = varianceRecipients == null ? null : java.util.List.copyOf(varianceRecipients);
        this.varianceSeverity = varianceSeverity;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceCondition() {
        return varianceCondition;
    }

    public double getVarianceThreshold() {
        return varianceThreshold;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public List<StepEntity> getVarianceRecipients() {
        return varianceRecipients;
    }

    public int getVarianceSeverity() {
        return varianceSeverity;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceCondition", varianceCondition);
        state.put("varianceThreshold", varianceThreshold);
        state.put("varianceActions", varianceActions);
        state.put("varianceRecipients", varianceRecipients);
        state.put("varianceSeverity", varianceSeverity);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
