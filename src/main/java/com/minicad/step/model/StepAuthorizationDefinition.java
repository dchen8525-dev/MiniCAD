package com.minicad.step.model;

import java.util.List;

/**
 * Resolved AUTHORIZATION_DEFINITION.
 * An authorization definition entity.
 *
 * @param id STEP instance id
 * @param name authorization name
 * @param authorizationType authorization variance type
 * @param authorizationPermissions authorization variance permissions
 * @param authorizationRoles authorization variance roles
 * @param authorizationConstraints authorization variance constraints
 * @param authorizationStatus authorization variance status
 */
public record StepAuthorizationDefinition(
    int id,
    String name,
    String authorizationType,
    List<String> authorizationPermissions,
    List<String> authorizationRoles,
    List<String> authorizationConstraints,
    String authorizationStatus) implements StepEntity {
}