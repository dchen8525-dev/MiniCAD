package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepNotificationSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final List<String> varianceEvents;
    private final List<StepEntity> varianceRecipients;
    private final String varianceMethod;
    private final int variancePriority;
    private final String varianceFormat;
    private final String varianceStatus;

    public StepNotificationSpecification(int id, String name, List<String> varianceEvents, List<StepEntity> varianceRecipients, String varianceMethod, int variancePriority, String varianceFormat, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceRecipients = varianceRecipients == null ? null : java.util.List.copyOf(varianceRecipients);
        this.varianceMethod = varianceMethod;
        this.variancePriority = variancePriority;
        this.varianceFormat = varianceFormat;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNotificationSpecification that = (StepNotificationSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEvents, that.varianceEvents) && Objects.equals(varianceRecipients, that.varianceRecipients) && Objects.equals(varianceMethod, that.varianceMethod) && variancePriority == that.variancePriority && Objects.equals(varianceFormat, that.varianceFormat) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEvents, varianceRecipients, varianceMethod, variancePriority, varianceFormat, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepNotificationSpecification{" + "id=" + id + "name=" + name + "varianceEvents=" + varianceEvents + "varianceRecipients=" + varianceRecipients + "varianceMethod=" + varianceMethod + "variancePriority=" + variancePriority + "varianceFormat=" + varianceFormat + "varianceStatus=" + varianceStatus + "}";
    }
}