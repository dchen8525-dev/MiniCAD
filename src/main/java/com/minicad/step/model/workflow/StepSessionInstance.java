package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SESSION_INSTANCE.
 * A session instance entity.
 *
 * @param id STEP instance id
 * @param name session instance name
 * @param sessionDefinition session variance definition reference
 * @param sessionState session variance state
 * @param sessionUser session variance user reference
 * @param sessionStartTime session variance start time
 * @param sessionLastActivity session variance last activity time
 * @param sessionStatus session variance status
 */
/**
 * Resolved SESSION_INSTANCE.
 * A session instance entity.
 *
 * @param id STEP instance id
 * @param name session instance name
 * @param sessionDefinition session variance definition reference
 * @param sessionState session variance state
 * @param sessionUser session variance user reference
 * @param sessionStartTime session variance start time
 * @param sessionLastActivity session variance last activity time
 * @param sessionStatus session variance status
 */
public final class StepSessionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sessionDefinition;
    private final String sessionState;
    private final StepEntity sessionUser;
    private final StepEntity sessionStartTime;
    private final StepEntity sessionLastActivity;
    private final String sessionStatus;

    public StepSessionInstance(int id, String name, StepEntity sessionDefinition, String sessionState, StepEntity sessionUser, StepEntity sessionStartTime, StepEntity sessionLastActivity, String sessionStatus) {
        this.id = id;
        this.name = name;
        this.sessionDefinition = sessionDefinition;
        this.sessionState = sessionState;
        this.sessionUser = sessionUser;
        this.sessionStartTime = sessionStartTime;
        this.sessionLastActivity = sessionLastActivity;
        this.sessionStatus = sessionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSessionDefinition() {
        return sessionDefinition;
    }

    public String getSessionState() {
        return sessionState;
    }

    public StepEntity getSessionUser() {
        return sessionUser;
    }

    public StepEntity getSessionStartTime() {
        return sessionStartTime;
    }

    public StepEntity getSessionLastActivity() {
        return sessionLastActivity;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSessionInstance that = (StepSessionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sessionDefinition, that.sessionDefinition) && Objects.equals(sessionState, that.sessionState) && Objects.equals(sessionUser, that.sessionUser) && Objects.equals(sessionStartTime, that.sessionStartTime) && Objects.equals(sessionLastActivity, that.sessionLastActivity) && Objects.equals(sessionStatus, that.sessionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sessionDefinition, sessionState, sessionUser, sessionStartTime, sessionLastActivity, sessionStatus);
    }

    @Override
    public String toString() {
        return "StepSessionInstance{" + "id=" + id + "name=" + name + "sessionDefinition=" + sessionDefinition + "sessionState=" + sessionState + "sessionUser=" + sessionUser + "sessionStartTime=" + sessionStartTime + "sessionLastActivity=" + sessionLastActivity + "sessionStatus=" + sessionStatus + "}";
    }
}