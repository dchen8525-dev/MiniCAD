package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ROLE_INSTANCE.
 * A role instance entity.
 *
 * @param id STEP instance id
 * @param name role instance name
 * @param roleDefinition role variance definition reference
 * @param roleHolder role variance holder reference
 * @param roleAssignedTime role variance assigned time
 * @param roleExpiration role variance expiration time
 * @param roleStatus role variance status
 */
/**
 * Resolved ROLE_INSTANCE.
 * A role instance entity.
 *
 * @param id STEP instance id
 * @param name role instance name
 * @param roleDefinition role variance definition reference
 * @param roleHolder role variance holder reference
 * @param roleAssignedTime role variance assigned time
 * @param roleExpiration role variance expiration time
 * @param roleStatus role variance status
 */
public final class StepRoleInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity roleDefinition;
    private final StepEntity roleHolder;
    private final StepEntity roleAssignedTime;
    private final StepEntity roleExpiration;
    private final String roleStatus;

    public StepRoleInstance(int id, String name, StepEntity roleDefinition, StepEntity roleHolder, StepEntity roleAssignedTime, StepEntity roleExpiration, String roleStatus) {
        this.id = id;
        this.name = name;
        this.roleDefinition = roleDefinition;
        this.roleHolder = roleHolder;
        this.roleAssignedTime = roleAssignedTime;
        this.roleExpiration = roleExpiration;
        this.roleStatus = roleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRoleDefinition() {
        return roleDefinition;
    }

    public StepEntity getRoleHolder() {
        return roleHolder;
    }

    public StepEntity getRoleAssignedTime() {
        return roleAssignedTime;
    }

    public StepEntity getRoleExpiration() {
        return roleExpiration;
    }

    public String getRoleStatus() {
        return roleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRoleInstance that = (StepRoleInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(roleDefinition, that.roleDefinition) && Objects.equals(roleHolder, that.roleHolder) && Objects.equals(roleAssignedTime, that.roleAssignedTime) && Objects.equals(roleExpiration, that.roleExpiration) && Objects.equals(roleStatus, that.roleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roleDefinition, roleHolder, roleAssignedTime, roleExpiration, roleStatus);
    }

    @Override
    public String toString() {
        return "StepRoleInstance{" + "id=" + id + "name=" + name + "roleDefinition=" + roleDefinition + "roleHolder=" + roleHolder + "roleAssignedTime=" + roleAssignedTime + "roleExpiration=" + roleExpiration + "roleStatus=" + roleStatus + "}";
    }
}