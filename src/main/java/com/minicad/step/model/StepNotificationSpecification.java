package com.minicad.step.model;

import java.util.List;

/**
 * Resolved NOTIFICATION_SPECIFICATION.
 * A notification specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceEvents notification variance events
 * @varianceRecipients notification variance recipients
 * @varianceMethod notification variance method (email, message, alert)
 * @variancePriority notification variance priority
 * @varianceFormat notification variance format
 * @varianceStatus specification variance status
 */
public record StepNotificationSpecification(
    int id,
    String name,
    List<String> varianceEvents,
    List<StepEntity> varianceRecipients,
    String varianceMethod,
    int variancePriority,
    String varianceFormat,
    String varianceStatus) implements StepEntity {
}