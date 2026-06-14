package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPermissionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity permissionDefinition;
    private final StepEntity permissionHolder;
    private final String permissionState;
    private final int permissionUsedCount;
    private final String permissionStatus;

    public StepPermissionInstance(int id, String name, StepEntity permissionDefinition, StepEntity permissionHolder, String permissionState, int permissionUsedCount, String permissionStatus) {
        this.id = id;
        this.name = name;
        this.permissionDefinition = permissionDefinition;
        this.permissionHolder = permissionHolder;
        this.permissionState = permissionState;
        this.permissionUsedCount = permissionUsedCount;
        this.permissionStatus = permissionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPermissionInstance that = (StepPermissionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(permissionDefinition, that.permissionDefinition) && Objects.equals(permissionHolder, that.permissionHolder) && Objects.equals(permissionState, that.permissionState) && permissionUsedCount == that.permissionUsedCount && Objects.equals(permissionStatus, that.permissionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, permissionDefinition, permissionHolder, permissionState, permissionUsedCount, permissionStatus);
    }

    @Override
    public String toString() {
        return "StepPermissionInstance{" + "id=" + id + "name=" + name + "permissionDefinition=" + permissionDefinition + "permissionHolder=" + permissionHolder + "permissionState=" + permissionState + "permissionUsedCount=" + permissionUsedCount + "permissionStatus=" + permissionStatus + "}";
    }
}