package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAuthorizationInstance(
    int id,
    String name,
    StepEntity authorizationDefinition,
    StepEntity authorizationHolder,
    String authorizationState,
    StepEntity authorizationGrantedTime,
    String authorizationStatus) implements StepEntity {
}