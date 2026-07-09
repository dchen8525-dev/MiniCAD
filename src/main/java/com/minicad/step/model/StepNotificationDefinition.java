package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNotificationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String notificationType;
    private final String notificationTrigger;
    private final List<String> notificationRecipients;
    private final String notificationMessage;
    private final String notificationStatus;

    public StepNotificationDefinition(int id, String name, String notificationType, String notificationTrigger, List<String> notificationRecipients, String notificationMessage, String notificationStatus) {
        this.id = id;
        this.name = name;
        this.notificationType = notificationType;
        this.notificationTrigger = notificationTrigger;
        this.notificationRecipients = notificationRecipients == null ? null : java.util.List.copyOf(notificationRecipients);
        this.notificationMessage = notificationMessage;
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

    public String getNotificationTrigger() {
        return notificationTrigger;
    }

    public List<String> getNotificationRecipients() {
        return notificationRecipients;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNotificationDefinition that = (StepNotificationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(notificationType, that.notificationType) && Objects.equals(notificationTrigger, that.notificationTrigger) && Objects.equals(notificationRecipients, that.notificationRecipients) && Objects.equals(notificationMessage, that.notificationMessage) && Objects.equals(notificationStatus, that.notificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notificationType, notificationTrigger, notificationRecipients, notificationMessage, notificationStatus);
    }

    @Override
    public String toString() {
        return "StepNotificationDefinition{" + "id=" + id + "name=" + name + "notificationType=" + notificationType + "notificationTrigger=" + notificationTrigger + "notificationRecipients=" + notificationRecipients + "notificationMessage=" + notificationMessage + "notificationStatus=" + notificationStatus + "}";
    }
}