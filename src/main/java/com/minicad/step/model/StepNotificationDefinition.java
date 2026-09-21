package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepNotificationDefinition extends AbstractStepEntity {
    private final String notificationType;
    private final String notificationTrigger;
    private final List<String> notificationRecipients;
    private final String notificationMessage;
    private final String notificationStatus;

    public StepNotificationDefinition(int id, String name, String notificationType, String notificationTrigger, List<String> notificationRecipients, String notificationMessage, String notificationStatus) {
        super(id, name);
        this.notificationType = notificationType;
        this.notificationTrigger = notificationTrigger;
        this.notificationRecipients = notificationRecipients == null ? null : java.util.List.copyOf(notificationRecipients);
        this.notificationMessage = notificationMessage;
        this.notificationStatus = notificationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("notificationType", notificationType);
        state.put("notificationTrigger", notificationTrigger);
        state.put("notificationRecipients", notificationRecipients);
        state.put("notificationMessage", notificationMessage);
        state.put("notificationStatus", notificationStatus);
        return state;
    }
}
