package com.minicad.step.model;

import java.util.List;

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
public record StepPermissionDefinition(
    int id,
    String name,
    String permissionType,
    String permissionScope,
    List<String> permissionActions,
    List<String> permissionConditions,
    String permissionStatus) implements StepEntity {
}