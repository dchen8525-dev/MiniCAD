package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNotificationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity notificationDefinition;
    private final String notificationSubject;
    private final String notificationBody;
    private final List<String> notificationRecipients;
    private final StepEntity notificationSentTime;
    private final String notificationStatus;

    public StepNotificationInstance(int id, String name, StepEntity notificationDefinition, String notificationSubject, String notificationBody, List<String> notificationRecipients, StepEntity notificationSentTime, String notificationStatus) {
        this.id = id;
        this.name = name;
        this.notificationDefinition = notificationDefinition;
        this.notificationSubject = notificationSubject;
        this.notificationBody = notificationBody;
        this.notificationRecipients = notificationRecipients == null ? null : java.util.List.copyOf(notificationRecipients);
        this.notificationSentTime = notificationSentTime;
        this.notificationStatus = notificationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getNotificationDefinition() {
        return notificationDefinition;
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public List<String> getNotificationRecipients() {
        return notificationRecipients;
    }

    public StepEntity getNotificationSentTime() {
        return notificationSentTime;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNotificationInstance that = (StepNotificationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(notificationDefinition, that.notificationDefinition) && Objects.equals(notificationSubject, that.notificationSubject) && Objects.equals(notificationBody, that.notificationBody) && Objects.equals(notificationRecipients, that.notificationRecipients) && Objects.equals(notificationSentTime, that.notificationSentTime) && Objects.equals(notificationStatus, that.notificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, notificationDefinition, notificationSubject, notificationBody, notificationRecipients, notificationSentTime, notificationStatus);
    }

    @Override
    public String toString() {
        return "StepNotificationInstance{" + "id=" + id + "name=" + name + "notificationDefinition=" + notificationDefinition + "notificationSubject=" + notificationSubject + "notificationBody=" + notificationBody + "notificationRecipients=" + notificationRecipients + "notificationSentTime=" + notificationSentTime + "notificationStatus=" + notificationStatus + "}";
    }
}