package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepPermissionInstance(
    int id,
    String name,
    StepEntity permissionDefinition,
    StepEntity permissionHolder,
    String permissionState,
    int permissionUsedCount,
    String permissionStatus) implements StepEntity {
}