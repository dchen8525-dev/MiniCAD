package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSessionInstance extends AbstractStepEntity {
    private final StepEntity sessionDefinition;
    private final String sessionState;
    private final StepEntity sessionUser;
    private final StepEntity sessionStartTime;
    private final StepEntity sessionLastActivity;
    private final String sessionStatus;

    public StepSessionInstance(int id, String name, StepEntity sessionDefinition, String sessionState, StepEntity sessionUser, StepEntity sessionStartTime, StepEntity sessionLastActivity, String sessionStatus) {
        super(id, name);
        this.sessionDefinition = sessionDefinition;
        this.sessionState = sessionState;
        this.sessionUser = sessionUser;
        this.sessionStartTime = sessionStartTime;
        this.sessionLastActivity = sessionLastActivity;
        this.sessionStatus = sessionStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sessionDefinition", sessionDefinition);
        state.put("sessionState", sessionState);
        state.put("sessionUser", sessionUser);
        state.put("sessionStartTime", sessionStartTime);
        state.put("sessionLastActivity", sessionLastActivity);
        state.put("sessionStatus", sessionStatus);
        return state;
    }
}
