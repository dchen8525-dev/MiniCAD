package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepErrorHandling extends AbstractStepEntity {
    private final String varianceError;
    private final String varianceCause;
    private final String varianceHandling;
    private final int varianceSeverity;
    private final String varianceRecovery;
    private final String varianceStatus;

    public StepErrorHandling(int id, String name, String varianceError, String varianceCause, String varianceHandling, int varianceSeverity, String varianceRecovery, String varianceStatus) {
        super(id, name);
        this.varianceError = varianceError;
        this.varianceCause = varianceCause;
        this.varianceHandling = varianceHandling;
        this.varianceSeverity = varianceSeverity;
        this.varianceRecovery = varianceRecovery;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceError", varianceError,
                "varianceCause", varianceCause, "varianceHandling", varianceHandling,
                "varianceSeverity", varianceSeverity, "varianceRecovery", varianceRecovery,
                "varianceStatus", varianceStatus);
    }
}
