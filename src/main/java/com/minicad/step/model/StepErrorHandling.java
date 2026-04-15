package com.minicad.step.model;

import java.util.List;

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
public record StepErrorHandling(
    int id,
    String name,
    String varianceError,
    String varianceCause,
    String varianceHandling,
    int varianceSeverity,
    String varianceRecovery,
    String varianceStatus) implements StepEntity {
}