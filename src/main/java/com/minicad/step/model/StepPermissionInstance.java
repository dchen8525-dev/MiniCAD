package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PERMISSION_INSTANCE.
 * A permission instance entity.
 *
 * @param id STEP instance id
 * @param name permission instance name
 * @param permissionDefinition permission variance definition reference
 * @param permissionHolder permission variance holder reference
 * @param permissionState permission variance state
 * @param permissionUsedCount permission variance usage count
 * @param permissionStatus permission variance status
 */
public final class StepPermissionInstance extends AbstractStepEntity {
    private final StepEntity permissionDefinition;
    private final StepEntity permissionHolder;
    private final String permissionState;
    private final int permissionUsedCount;
    private final String permissionStatus;

    public StepPermissionInstance(int id, String name, StepEntity permissionDefinition, StepEntity permissionHolder, String permissionState, int permissionUsedCount, String permissionStatus) {
        super(id, name);
        this.permissionDefinition = permissionDefinition;
        this.permissionHolder = permissionHolder;
        this.permissionState = permissionState;
        this.permissionUsedCount = permissionUsedCount;
        this.permissionStatus = permissionStatus;
    }

    public StepEntity getPermissionDefinition() {
        return permissionDefinition;
    }

    public StepEntity getPermissionHolder() {
        return permissionHolder;
    }

    public String getPermissionState() {
        return permissionState;
    }

    public int getPermissionUsedCount() {
        return permissionUsedCount;
    }

    public String getPermissionStatus() {
        return permissionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("permissionDefinition", permissionDefinition);
        state.put("permissionHolder", permissionHolder);
        state.put("permissionState", permissionState);
        state.put("permissionUsedCount", permissionUsedCount);
        state.put("permissionStatus", permissionStatus);
        return state;
    }
}
