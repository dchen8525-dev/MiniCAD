package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ROLE_DEFINITION.
 * A role definition entity.
 *
 * @param id STEP instance id
 * @param name role name
 * @param roleType role variance type
 * @param roleDescription role variance description
 * @param rolePermissions role variance permissions
 * @param roleResponsibilities role variance responsibilities
 * @param roleStatus role variance status
 */
/**
 * Resolved ROLE_DEFINITION.
 * A role definition entity.
 *
 * @param id STEP instance id
 * @param name role name
 * @param roleType role variance type
 * @param roleDescription role variance description
 * @param rolePermissions role variance permissions
 * @param roleResponsibilities role variance responsibilities
 * @param roleStatus role variance status
 */
public final class StepRoleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String roleType;
    private final String roleDescription;
    private final List<StepEntity> rolePermissions;
    private final List<String> roleResponsibilities;
    private final String roleStatus;

    public StepRoleDefinition(int id, String name, String roleType, String roleDescription, List<StepEntity> rolePermissions, List<String> roleResponsibilities, String roleStatus) {
        this.id = id;
        this.name = name;
        this.roleType = roleType;
        this.roleDescription = roleDescription;
        this.rolePermissions = rolePermissions == null ? null : java.util.List.copyOf(rolePermissions);
        this.roleResponsibilities = roleResponsibilities == null ? null : java.util.List.copyOf(roleResponsibilities);
        this.roleStatus = roleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public List<StepEntity> getRolePermissions() {
        return rolePermissions;
    }

    public List<String> getRoleResponsibilities() {
        return roleResponsibilities;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRoleDefinition that = (StepRoleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(roleType, that.roleType) && Objects.equals(roleDescription, that.roleDescription) && Objects.equals(rolePermissions, that.rolePermissions) && Objects.equals(roleResponsibilities, that.roleResponsibilities) && Objects.equals(roleStatus, that.roleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roleType, roleDescription, rolePermissions, roleResponsibilities, roleStatus);
    }

    @Override
    public String toString() {
        return "StepRoleDefinition{" + "id=" + id + "name=" + name + "roleType=" + roleType + "roleDescription=" + roleDescription + "rolePermissions=" + rolePermissions + "roleResponsibilities=" + roleResponsibilities + "roleStatus=" + roleStatus + "}";
    }
}