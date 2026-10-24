package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepNotificationRecord extends AbstractStepEntity {
    private final String notificationType;
    private final StepEntity notificationSender;
    private final StepEntity notificationReceiver;
    private final StepEntity notificationTime;
    private final String notificationContent;
    private final boolean notificationDelivered;
    private final String notificationStatus;

    public StepNotificationRecord(int id, String name, String notificationType, StepEntity notificationSender, StepEntity notificationReceiver, StepEntity notificationTime, String notificationContent, boolean notificationDelivered, String notificationStatus) {
        super(id, name);
        this.notificationType = notificationType;
        this.notificationSender = notificationSender;
        this.notificationReceiver = notificationReceiver;
        this.notificationTime = notificationTime;
        this.notificationContent = notificationContent;
        this.notificationDelivered = notificationDelivered;
        this.notificationStatus = notificationStatus;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public StepEntity getNotificationSender() {
        return notificationSender;
    }

    public StepEntity getNotificationReceiver() {
        return notificationReceiver;
    }

    public StepEntity getNotificationTime() {
        return notificationTime;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public boolean isNotificationDelivered() {
        return notificationDelivered;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "notificationType", notificationType,
                "notificationSender", notificationSender, "notificationReceiver", notificationReceiver,
                "notificationTime", notificationTime, "notificationContent", notificationContent,
                "notificationDelivered", notificationDelivered, "notificationStatus", notificationStatus);
    }
}
