package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ACCESS_CONTROL.
 * An access control entity.
 *
 * @param id STEP instance id
 * @param name control name
 * @varianceRoles access variance roles
 * @variancePermissions access variance permissions
 * @varianceResources protected variance resources
 * @variancePolicy access variance policy
 * @varianceStatus control variance status
 */
public record StepAccessControl(
    int id,
    String name,
    List<StepEntity> varianceRoles,
    List<String> variancePermissions,
    List<StepEntity> varianceResources,
    String variancePolicy,
    String varianceStatus) implements StepEntity {
}