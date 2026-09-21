package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPermissionDefinition extends AbstractStepEntity {
    private final String permissionType;
    private final String permissionScope;
    private final List<String> permissionActions;
    private final List<String> permissionConditions;
    private final String permissionStatus;

    public StepPermissionDefinition(int id, String name, String permissionType, String permissionScope, List<String> permissionActions, List<String> permissionConditions, String permissionStatus) {
        super(id, name);
        this.permissionType = permissionType;
        this.permissionScope = permissionScope;
        this.permissionActions = permissionActions == null ? null : java.util.List.copyOf(permissionActions);
        this.permissionConditions = permissionConditions == null ? null : java.util.List.copyOf(permissionConditions);
        this.permissionStatus = permissionStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("permissionType", permissionType);
        state.put("permissionScope", permissionScope);
        state.put("permissionActions", permissionActions);
        state.put("permissionConditions", permissionConditions);
        state.put("permissionStatus", permissionStatus);
        return state;
    }
}
