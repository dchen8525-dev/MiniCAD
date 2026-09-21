package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRoleDefinition extends AbstractStepEntity {
    private final String roleType;
    private final String roleDescription;
    private final List<StepEntity> rolePermissions;
    private final List<String> roleResponsibilities;
    private final String roleStatus;

    public StepRoleDefinition(int id, String name, String roleType, String roleDescription, List<StepEntity> rolePermissions, List<String> roleResponsibilities, String roleStatus) {
        super(id, name);
        this.roleType = roleType;
        this.roleDescription = roleDescription;
        this.rolePermissions = rolePermissions == null ? null : java.util.List.copyOf(rolePermissions);
        this.roleResponsibilities = roleResponsibilities == null ? null : java.util.List.copyOf(roleResponsibilities);
        this.roleStatus = roleStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("roleType", roleType);
        state.put("roleDescription", roleDescription);
        state.put("rolePermissions", rolePermissions);
        state.put("roleResponsibilities", roleResponsibilities);
        state.put("roleStatus", roleStatus);
        return state;
    }
}
