package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAlertConfiguration implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceCondition;
    private final double varianceThreshold;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceRecipients;
    private final int varianceSeverity;
    private final String varianceStatus;

    public StepAlertConfiguration(int id, String name, String varianceCondition, double varianceThreshold, List<StepEntity> varianceActions, List<StepEntity> varianceRecipients, int varianceSeverity, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceCondition = varianceCondition;
        this.varianceThreshold = varianceThreshold;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceRecipients = varianceRecipients == null ? null : java.util.List.copyOf(varianceRecipients);
        this.varianceSeverity = varianceSeverity;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAlertConfiguration that = (StepAlertConfiguration) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceCondition, that.varianceCondition) && varianceThreshold == that.varianceThreshold && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceRecipients, that.varianceRecipients) && varianceSeverity == that.varianceSeverity && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceCondition, varianceThreshold, varianceActions, varianceRecipients, varianceSeverity, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAlertConfiguration{" + "id=" + id + "name=" + name + "varianceCondition=" + varianceCondition + "varianceThreshold=" + varianceThreshold + "varianceActions=" + varianceActions + "varianceRecipients=" + varianceRecipients + "varianceSeverity=" + varianceSeverity + "varianceStatus=" + varianceStatus + "}";
    }
}