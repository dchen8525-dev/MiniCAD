package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved AUTHORIZATION_INSTANCE.
 * An authorization instance entity.
 *
 * @param id STEP instance id
 * @param name authorization instance name
 * @param authorizationDefinition authorization variance definition reference
 * @param authorizationHolder authorization variance holder reference
 * @param authorizationState authorization variance state
 * @param authorizationGrantedTime authorization variance granted time
 * @param authorizationStatus authorization variance status
 */
public final class StepAuthorizationInstance extends AbstractStepEntity {
    private final StepEntity authorizationDefinition;
    private final StepEntity authorizationHolder;
    private final String authorizationState;
    private final StepEntity authorizationGrantedTime;
    private final String authorizationStatus;

    public StepAuthorizationInstance(int id, String name, StepEntity authorizationDefinition, StepEntity authorizationHolder, String authorizationState, StepEntity authorizationGrantedTime, String authorizationStatus) {
        super(id, name);
        this.authorizationDefinition = authorizationDefinition;
        this.authorizationHolder = authorizationHolder;
        this.authorizationState = authorizationState;
        this.authorizationGrantedTime = authorizationGrantedTime;
        this.authorizationStatus = authorizationStatus;
    }

    public StepEntity getAuthorizationDefinition() {
        return authorizationDefinition;
    }

    public StepEntity getAuthorizationHolder() {
        return authorizationHolder;
    }

    public String getAuthorizationState() {
        return authorizationState;
    }

    public StepEntity getAuthorizationGrantedTime() {
        return authorizationGrantedTime;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("authorizationDefinition", authorizationDefinition);
        state.put("authorizationHolder", authorizationHolder);
        state.put("authorizationState", authorizationState);
        state.put("authorizationGrantedTime", authorizationGrantedTime);
        state.put("authorizationStatus", authorizationStatus);
        return state;
    }
}
