package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved NOTIFICATION_RECORD.
 * A notification record entity.
 *
 * @param id STEP instance id
 * @param name notification name
 * @param notificationType notification variance type
 * @param notificationSender notification variance sender reference
 * @param notificationReceiver notification variance receiver reference
 * @param notificationTime notification variance sent time
 * @param notificationContent notification variance content
 * @param notificationDelivered notification variance delivered flag
 * @param notificationStatus notification variance status
 */
public record StepNotificationRecord(
    int id,
    String name,
    String notificationType,
    StepEntity notificationSender,
    StepEntity notificationReceiver,
    StepEntity notificationTime,
    String notificationContent,
    boolean notificationDelivered,
    String notificationStatus) implements StepEntity {
}