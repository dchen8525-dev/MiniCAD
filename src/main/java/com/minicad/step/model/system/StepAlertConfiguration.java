package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAlertConfiguration(
    int id,
    String name,
    String varianceCondition,
    double varianceThreshold,
    List<StepEntity> varianceActions,
    List<StepEntity> varianceRecipients,
    int varianceSeverity,
    String varianceStatus) implements StepEntity {
}