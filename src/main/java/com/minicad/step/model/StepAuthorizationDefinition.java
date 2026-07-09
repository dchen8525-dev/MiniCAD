package com.minicad.step.model.management.security;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAuthorizationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String authorizationType;
    private final List<String> authorizationPermissions;
    private final List<String> authorizationRoles;
    private final List<String> authorizationConstraints;
    private final String authorizationStatus;

    public StepAuthorizationDefinition(int id, String name, String authorizationType, List<String> authorizationPermissions, List<String> authorizationRoles, List<String> authorizationConstraints, String authorizationStatus) {
        this.id = id;
        this.name = name;
        this.authorizationType = authorizationType;
        this.authorizationPermissions = authorizationPermissions == null ? null : java.util.List.copyOf(authorizationPermissions);
        this.authorizationRoles = authorizationRoles == null ? null : java.util.List.copyOf(authorizationRoles);
        this.authorizationConstraints = authorizationConstraints == null ? null : java.util.List.copyOf(authorizationConstraints);
        this.authorizationStatus = authorizationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAuthorizationDefinition that = (StepAuthorizationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(authorizationType, that.authorizationType) && Objects.equals(authorizationPermissions, that.authorizationPermissions) && Objects.equals(authorizationRoles, that.authorizationRoles) && Objects.equals(authorizationConstraints, that.authorizationConstraints) && Objects.equals(authorizationStatus, that.authorizationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorizationType, authorizationPermissions, authorizationRoles, authorizationConstraints, authorizationStatus);
    }

    @Override
    public String toString() {
        return "StepAuthorizationDefinition{" + "id=" + id + "name=" + name + "authorizationType=" + authorizationType + "authorizationPermissions=" + authorizationPermissions + "authorizationRoles=" + authorizationRoles + "authorizationConstraints=" + authorizationConstraints + "authorizationStatus=" + authorizationStatus + "}";
    }
}