package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepTimeoutDefinition2 extends AbstractStepEntity {
    private final String timeoutType;
    private final int timeoutDuration;
    private final StepEntity timeoutAction;
    private final int timeoutGracePeriod;
    private final String timeoutStatus;

    public StepTimeoutDefinition2(int id, String name, String timeoutType, int timeoutDuration, StepEntity timeoutAction, int timeoutGracePeriod, String timeoutStatus) {
        super(id, name);
        this.timeoutType = timeoutType;
        this.timeoutDuration = timeoutDuration;
        this.timeoutAction = timeoutAction;
        this.timeoutGracePeriod = timeoutGracePeriod;
        this.timeoutStatus = timeoutStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "timeoutType", timeoutType,
                "timeoutDuration", timeoutDuration, "timeoutAction", timeoutAction,
                "timeoutGracePeriod", timeoutGracePeriod, "timeoutStatus", timeoutStatus);
    }
}
