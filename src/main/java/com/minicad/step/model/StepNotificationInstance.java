package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepNotificationInstance extends AbstractStepEntity {
    private final StepEntity notificationDefinition;
    private final String notificationSubject;
    private final String notificationBody;
    private final List<String> notificationRecipients;
    private final StepEntity notificationSentTime;
    private final String notificationStatus;

    public StepNotificationInstance(int id, String name, StepEntity notificationDefinition, String notificationSubject, String notificationBody, List<String> notificationRecipients, StepEntity notificationSentTime, String notificationStatus) {
        super(id, name);
        this.notificationDefinition = notificationDefinition;
        this.notificationSubject = notificationSubject;
        this.notificationBody = notificationBody;
        this.notificationRecipients = notificationRecipients == null ? null : java.util.List.copyOf(notificationRecipients);
        this.notificationSentTime = notificationSentTime;
        this.notificationStatus = notificationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("notificationDefinition", notificationDefinition);
        state.put("notificationSubject", notificationSubject);
        state.put("notificationBody", notificationBody);
        state.put("notificationRecipients", notificationRecipients);
        state.put("notificationSentTime", notificationSentTime);
        state.put("notificationStatus", notificationStatus);
        return state;
    }
}
