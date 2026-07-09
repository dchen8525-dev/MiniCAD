package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PERMISSION_DEFINITION.
 * A permission definition entity.
 *
 * @param id STEP instance id
 * @param name permission name
 * @param permissionType permission variance type
 * @param permissionScope permission variance scope
 * @param permissionActions permission variance allowed actions
 * @param permissionConditions permission variance conditions
 * @param permissionStatus permission variance status
 */
/**
 * Resolved PERMISSION_DEFINITION.
 * A permission definition entity.
 *
 * @param id STEP instance id
 * @param name permission name
 * @param permissionType permission variance type
 * @param permissionScope permission variance scope
 * @param permissionActions permission variance allowed actions
 * @param permissionConditions permission variance conditions
 * @param permissionStatus permission variance status
 */
public final class StepPermissionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String permissionType;
    private final String permissionScope;
    private final List<String> permissionActions;
    private final List<String> permissionConditions;
    private final String permissionStatus;

    public StepPermissionDefinition(int id, String name, String permissionType, String permissionScope, List<String> permissionActions, List<String> permissionConditions, String permissionStatus) {
        this.id = id;
        this.name = name;
        this.permissionType = permissionType;
        this.permissionScope = permissionScope;
        this.permissionActions = permissionActions == null ? null : java.util.List.copyOf(permissionActions);
        this.permissionConditions = permissionConditions == null ? null : java.util.List.copyOf(permissionConditions);
        this.permissionStatus = permissionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public String getPermissionScope() {
        return permissionScope;
    }

    public List<String> getPermissionActions() {
        return permissionActions;
    }

    public List<String> getPermissionConditions() {
        return permissionConditions;
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPermissionDefinition that = (StepPermissionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(permissionType, that.permissionType) && Objects.equals(permissionScope, that.permissionScope) && Objects.equals(permissionActions, that.permissionActions) && Objects.equals(permissionConditions, that.permissionConditions) && Objects.equals(permissionStatus, that.permissionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, permissionType, permissionScope, permissionActions, permissionConditions, permissionStatus);
    }

    @Override
    public String toString() {
        return "StepPermissionDefinition{" + "id=" + id + "name=" + name + "permissionType=" + permissionType + "permissionScope=" + permissionScope + "permissionActions=" + permissionActions + "permissionConditions=" + permissionConditions + "permissionStatus=" + permissionStatus + "}";
    }
}