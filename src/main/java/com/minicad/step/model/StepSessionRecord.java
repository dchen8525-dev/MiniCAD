package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSessionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String sessionType;
    private final StepEntity sessionHolder;
    private final StepEntity sessionStartTime;
    private final StepEntity sessionEndTime;
    private final int sessionDuration;
    private final String sessionStatus;

    public StepSessionRecord(int id, String name, String sessionType, StepEntity sessionHolder, StepEntity sessionStartTime, StepEntity sessionEndTime, int sessionDuration, String sessionStatus) {
        this.id = id;
        this.name = name;
        this.sessionType = sessionType;
        this.sessionHolder = sessionHolder;
        this.sessionStartTime = sessionStartTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionDuration = sessionDuration;
        this.sessionStatus = sessionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSessionRecord that = (StepSessionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sessionType, that.sessionType) && Objects.equals(sessionHolder, that.sessionHolder) && Objects.equals(sessionStartTime, that.sessionStartTime) && Objects.equals(sessionEndTime, that.sessionEndTime) && sessionDuration == that.sessionDuration && Objects.equals(sessionStatus, that.sessionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sessionType, sessionHolder, sessionStartTime, sessionEndTime, sessionDuration, sessionStatus);
    }

    @Override
    public String toString() {
        return "StepSessionRecord{" + "id=" + id + "name=" + name + "sessionType=" + sessionType + "sessionHolder=" + sessionHolder + "sessionStartTime=" + sessionStartTime + "sessionEndTime=" + sessionEndTime + "sessionDuration=" + sessionDuration + "sessionStatus=" + sessionStatus + "}";
    }
}