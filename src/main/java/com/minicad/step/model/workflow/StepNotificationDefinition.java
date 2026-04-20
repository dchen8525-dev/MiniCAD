package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NOTIFICATION_DEFINITION.
 * A notification definition entity.
 *
 * @param id STEP instance id
 * @param name notification name
 * @param notificationType notification variance type
 * @param notificationTrigger notification variance trigger condition
 * @param notificationRecipients notification variance recipients
 * @param notificationMessage notification variance message template
 * @param notificationStatus notification variance status
 */
public record StepNotificationDefinition(
    int id,
    String name,
    String notificationType,
    String notificationTrigger,
    List<String> notificationRecipients,
    String notificationMessage,
    String notificationStatus) implements StepEntity {
}