package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepRoleDefinition(
    int id,
    String name,
    String roleType,
    String roleDescription,
    List<StepEntity> rolePermissions,
    List<String> roleResponsibilities,
    String roleStatus) implements StepEntity {
}