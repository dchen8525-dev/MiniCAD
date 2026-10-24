package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved SESSION_RECORD.
 * A session record entity.
 *
 * @param id STEP instance id
 * @param name session name
 * @param sessionType session variance type
 * @param sessionHolder session variance holder reference
 * @param sessionStartTime session variance start time
 * @param sessionEndTime session variance end time
 * @param sessionDuration session variance duration
 * @param sessionStatus session variance status
 */
public final class StepSessionRecord extends AbstractStepEntity {
    private final String sessionType;
    private final StepEntity sessionHolder;
    private final StepEntity sessionStartTime;
    private final StepEntity sessionEndTime;
    private final int sessionDuration;
    private final String sessionStatus;

    public StepSessionRecord(int id, String name, String sessionType, StepEntity sessionHolder, StepEntity sessionStartTime, StepEntity sessionEndTime, int sessionDuration, String sessionStatus) {
        super(id, name);
        this.sessionType = sessionType;
        this.sessionHolder = sessionHolder;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionDuration = sessionDuration;
        this.sessionStatus = sessionStatus;
    }

    public String getSessionType() {
        return sessionType;
    }

    public StepEntity getSessionHolder() {
        return sessionHolder;
    }

    public StepEntity getSessionStartTime() {
        return sessionStartTime;
    }

    public StepEntity getSessionEndTime() {
        return sessionEndTime;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sessionType", sessionType, "sessionHolder", sessionHolder,
                "sessionStartTime", sessionStartTime, "sessionEndTime", sessionEndTime,
                "sessionDuration", sessionDuration, "sessionStatus", sessionStatus);
    }
}
