package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuthorizationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity authorizationDefinition;
    private final StepEntity authorizationHolder;
    private final String authorizationState;
    private final StepEntity authorizationGrantedTime;
    private final String authorizationStatus;

    public StepAuthorizationInstance(int id, String name, StepEntity authorizationDefinition, StepEntity authorizationHolder, String authorizationState, StepEntity authorizationGrantedTime, String authorizationStatus) {
        this.id = id;
        this.name = name;
        this.authorizationDefinition = authorizationDefinition;
        this.authorizationHolder = authorizationHolder;
        this.authorizationState = authorizationState;
        this.authorizationGrantedTime = authorizationGrantedTime;
        this.authorizationStatus = authorizationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuthorizationInstance that = (StepAuthorizationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(authorizationDefinition, that.authorizationDefinition) && Objects.equals(authorizationHolder, that.authorizationHolder) && Objects.equals(authorizationState, that.authorizationState) && Objects.equals(authorizationGrantedTime, that.authorizationGrantedTime) && Objects.equals(authorizationStatus, that.authorizationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorizationDefinition, authorizationHolder, authorizationState, authorizationGrantedTime, authorizationStatus);
    }

    @Override
    public String toString() {
        return "StepAuthorizationInstance{" + "id=" + id + "name=" + name + "authorizationDefinition=" + authorizationDefinition + "authorizationHolder=" + authorizationHolder + "authorizationState=" + authorizationState + "authorizationGrantedTime=" + authorizationGrantedTime + "authorizationStatus=" + authorizationStatus + "}";
    }
}