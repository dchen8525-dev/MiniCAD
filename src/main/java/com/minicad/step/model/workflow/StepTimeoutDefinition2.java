package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TIMEOUT_DEFINITION.
 * A timeout definition entity.
 *
 * @param id STEP instance id
 * @param name timeout name
 * @param timeoutType timeout variance type
 * @param timeoutDuration timeout variance duration in seconds
 * @param timeoutAction timeout variance action on timeout
 * @param timeoutGracePeriod timeout variance grace period
 * @param timeoutStatus timeout variance status
 */
/**
 * Resolved TIMEOUT_DEFINITION.
 * A timeout definition entity.
 *
 * @param id STEP instance id
 * @param name timeout name
 * @param timeoutType timeout variance type
 * @param timeoutDuration timeout variance duration in seconds
 * @param timeoutAction timeout variance action on timeout
 * @param timeoutGracePeriod timeout variance grace period
 * @param timeoutStatus timeout variance status
 */
public final class StepTimeoutDefinition2 implements StepEntity {
    private final int id;
    private final String name;
    private final String timeoutType;
    private final int timeoutDuration;
    private final StepEntity timeoutAction;
    private final int timeoutGracePeriod;
    private final String timeoutStatus;

    public StepTimeoutDefinition2(int id, String name, String timeoutType, int timeoutDuration, StepEntity timeoutAction, int timeoutGracePeriod, String timeoutStatus) {
        this.id = id;
        this.name = name;
        this.timeoutType = timeoutType;
        this.timeoutDuration = timeoutDuration;
        this.timeoutAction = timeoutAction;
        this.timeoutGracePeriod = timeoutGracePeriod;
        this.timeoutStatus = timeoutStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimeoutType() {
        return timeoutType;
    }

    public int getTimeoutDuration() {
        return timeoutDuration;
    }

    public StepEntity getTimeoutAction() {
        return timeoutAction;
    }

    public int getTimeoutGracePeriod() {
        return timeoutGracePeriod;
    }

    public String getTimeoutStatus() {
        return timeoutStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTimeoutDefinition2 that = (StepTimeoutDefinition2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(timeoutType, that.timeoutType) && timeoutDuration == that.timeoutDuration && Objects.equals(timeoutAction, that.timeoutAction) && timeoutGracePeriod == that.timeoutGracePeriod && Objects.equals(timeoutStatus, that.timeoutStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, timeoutType, timeoutDuration, timeoutAction, timeoutGracePeriod, timeoutStatus);
    }

    @Override
    public String toString() {
        return "StepTimeoutDefinition2{" + "id=" + id + "name=" + name + "timeoutType=" + timeoutType + "timeoutDuration=" + timeoutDuration + "timeoutAction=" + timeoutAction + "timeoutGracePeriod=" + timeoutGracePeriod + "timeoutStatus=" + timeoutStatus + "}";
    }
}