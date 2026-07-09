package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ERROR_HANDLING.
 * An error handling entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @varianceError error variance type/code
 * @varianceCause error variance cause
 * @varianceHandling handling variance action
 * @varianceSeverity severity variance level
 * @varianceRecovery recovery variance procedure
 * @varianceStatus handling variance status
 */
/**
 * Resolved ERROR_HANDLING.
 * An error handling entity.
 *
 * @param id STEP instance id
 * @param name handling name
 * @varianceError error variance type/code
 * @varianceCause error variance cause
 * @varianceHandling handling variance action
 * @varianceSeverity severity variance level
 * @varianceRecovery recovery variance procedure
 * @varianceStatus handling variance status
 */
public final class StepErrorHandling implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceError;
    private final String varianceCause;
    private final String varianceHandling;
    private final int varianceSeverity;
    private final String varianceRecovery;
    private final String varianceStatus;

    public StepErrorHandling(int id, String name, String varianceError, String varianceCause, String varianceHandling, int varianceSeverity, String varianceRecovery, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceError = varianceError;
        this.varianceCause = varianceCause;
        this.varianceHandling = varianceHandling;
        this.varianceSeverity = varianceSeverity;
        this.varianceRecovery = varianceRecovery;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVarianceError() {
        return varianceError;
    }

    public String getVarianceCause() {
        return varianceCause;
    }

    public String getVarianceHandling() {
        return varianceHandling;
    }

    public int getVarianceSeverity() {
        return varianceSeverity;
    }

    public String getVarianceRecovery() {
        return varianceRecovery;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepErrorHandling that = (StepErrorHandling) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceError, that.varianceError) && Objects.equals(varianceCause, that.varianceCause) && Objects.equals(varianceHandling, that.varianceHandling) && varianceSeverity == that.varianceSeverity && Objects.equals(varianceRecovery, that.varianceRecovery) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceError, varianceCause, varianceHandling, varianceSeverity, varianceRecovery, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepErrorHandling{" + "id=" + id + "name=" + name + "varianceError=" + varianceError + "varianceCause=" + varianceCause + "varianceHandling=" + varianceHandling + "varianceSeverity=" + varianceSeverity + "varianceRecovery=" + varianceRecovery + "varianceStatus=" + varianceStatus + "}";
    }
}