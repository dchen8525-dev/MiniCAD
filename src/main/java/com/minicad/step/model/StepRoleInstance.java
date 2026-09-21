package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRoleInstance extends AbstractStepEntity {
    private final StepEntity roleDefinition;
    private final StepEntity roleHolder;
    private final StepEntity roleAssignedTime;
    private final StepEntity roleExpiration;
    private final String roleStatus;

    public StepRoleInstance(int id, String name, StepEntity roleDefinition, StepEntity roleHolder, StepEntity roleAssignedTime, StepEntity roleExpiration, String roleStatus) {
        super(id, name);
        this.roleDefinition = roleDefinition;
        this.roleHolder = roleHolder;
        this.roleAssignedTime = roleAssignedTime;
        this.roleExpiration = roleExpiration;
        this.roleStatus = roleStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("roleDefinition", roleDefinition);
        state.put("roleHolder", roleHolder);
        state.put("roleAssignedTime", roleAssignedTime);
        state.put("roleExpiration", roleExpiration);
        state.put("roleStatus", roleStatus);
        return state;
    }
}
