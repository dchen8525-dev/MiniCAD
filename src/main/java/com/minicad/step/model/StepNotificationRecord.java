package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNotificationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String notificationType;
    private final StepEntity notificationSender;
    private final StepEntity notificationReceiver;
    private final StepEntity notificationTime;
    private final String notificationContent;
    private final boolean notificationDelivered;
    private final String notificationStatus;

    public StepNotificationRecord(int id, String name, String notificationType, StepEntity notificationSender, StepEntity notificationReceiver, StepEntity notificationTime, String notificationContent, boolean notificationDelivered, String notificationStatus) {
        this.id = id;
        this.name = name;
        this.notificationType = notificationType;
        this.notificationSender = notificationSender;
        this.notificationReceiver = notificationReceiver;
        this.notificationTime = notificationTime;
        this.notificationContent = notificationContent;
        this.notificationDelivered = notificationDelivered;
        this.notificationStatus = notificationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNotificationRecord that = (StepNotificationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(notificationType, that.notificationType) && Objects.equals(notificationSender, that.notificationSender) && Objects.equals(notificationReceiver, that.notificationReceiver) && Objects.equals(notificationTime, that.notificationTime) && Objects.equals(notificationContent, that.notificationContent) && notificationDelivered == that.notificationDelivered && Objects.equals(notificationStatus, that.notificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notificationType, notificationSender, notificationReceiver, notificationTime, notificationContent, notificationDelivered, notificationStatus);
    }

    @Override
    public String toString() {
        return "StepNotificationRecord{" + "id=" + id + "name=" + name + "notificationType=" + notificationType + "notificationSender=" + notificationSender + "notificationReceiver=" + notificationReceiver + "notificationTime=" + notificationTime + "notificationContent=" + notificationContent + "notificationDelivered=" + notificationDelivered + "notificationStatus=" + notificationStatus + "}";
    }
}