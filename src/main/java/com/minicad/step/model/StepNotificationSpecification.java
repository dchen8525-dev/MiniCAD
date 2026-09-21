package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepNotificationSpecification extends AbstractStepEntity {
    private final List<String> varianceEvents;
    private final List<StepEntity> varianceRecipients;
    private final String varianceMethod;
    private final int variancePriority;
    private final String varianceFormat;
    private final String varianceStatus;

    public StepNotificationSpecification(int id, String name, List<String> varianceEvents, List<StepEntity> varianceRecipients, String varianceMethod, int variancePriority, String varianceFormat, String varianceStatus) {
        super(id, name);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceRecipients = varianceRecipients == null ? null : java.util.List.copyOf(varianceRecipients);
        this.varianceMethod = varianceMethod;
        this.variancePriority = variancePriority;
        this.varianceFormat = varianceFormat;
        this.varianceStatus = varianceStatus;
    }

    public List<String> getVarianceEvents() {
        return varianceEvents;
    }

    public List<StepEntity> getVarianceRecipients() {
        return varianceRecipients;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEvents", varianceEvents);
        state.put("varianceRecipients", varianceRecipients);
        state.put("varianceMethod", varianceMethod);
        state.put("variancePriority", variancePriority);
        state.put("varianceFormat", varianceFormat);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
