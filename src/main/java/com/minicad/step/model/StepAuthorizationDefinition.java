package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepAuthorizationDefinition extends AbstractStepEntity {
    private final String authorizationType;
    private final List<String> authorizationPermissions;
    private final List<String> authorizationRoles;
    private final List<String> authorizationConstraints;
    private final String authorizationStatus;

    public StepAuthorizationDefinition(int id, String name, String authorizationType, List<String> authorizationPermissions, List<String> authorizationRoles, List<String> authorizationConstraints, String authorizationStatus) {
        super(id, name);
        this.authorizationType = authorizationType;
        this.authorizationPermissions = authorizationPermissions == null ? null : java.util.List.copyOf(authorizationPermissions);
        this.authorizationRoles = authorizationRoles == null ? null : java.util.List.copyOf(authorizationRoles);
        this.authorizationConstraints = authorizationConstraints == null ? null : java.util.List.copyOf(authorizationConstraints);
        this.authorizationStatus = authorizationStatus;
    }

    public String getAuthorizationType() {
        return authorizationType;
    }

    public List<String> getAuthorizationPermissions() {
        return authorizationPermissions;
    }

    public List<String> getAuthorizationRoles() {
        return authorizationRoles;
    }

    public List<String> getAuthorizationConstraints() {
        return authorizationConstraints;
    }

    public String getAuthorizationStatus() {
        return authorizationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("authorizationType", authorizationType);
        state.put("authorizationPermissions", authorizationPermissions);
        state.put("authorizationRoles", authorizationRoles);
        state.put("authorizationConstraints", authorizationConstraints);
        state.put("authorizationStatus", authorizationStatus);
        return state;
    }
}
