package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepRoleInstance(
    int id,
    String name,
    StepEntity roleDefinition,
    StepEntity roleHolder,
    StepEntity roleAssignedTime,
    StepEntity roleExpiration,
    String roleStatus) implements StepEntity {
}