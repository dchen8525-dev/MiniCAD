package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NOTIFICATION_INSTANCE.
 * A notification instance entity.
 *
 * @param id STEP instance id
 * @param name notification instance name
 * @param notificationDefinition notification variance definition reference
 * @param notificationSubject notification variance subject
 * @param notificationBody notification variance body content
 * @param notificationRecipients notification variance actual recipients
 * @param notificationSentTime notification variance sent time
 * @param notificationStatus notification variance status
 */
public record StepNotificationInstance(
    int id,
    String name,
    StepEntity notificationDefinition,
    String notificationSubject,
    String notificationBody,
    List<String> notificationRecipients,
    StepEntity notificationSentTime,
    String notificationStatus) implements StepEntity {
}