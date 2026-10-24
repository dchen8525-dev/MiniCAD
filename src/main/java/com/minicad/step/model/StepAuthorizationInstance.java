package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "authorizationDefinition", authorizationDefinition,
                "authorizationHolder", authorizationHolder, "authorizationState", authorizationState,
                "authorizationGrantedTime", authorizationGrantedTime,
                "authorizationStatus", authorizationStatus);
    }
}
