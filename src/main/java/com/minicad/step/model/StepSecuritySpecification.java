package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SECURITY_SPECIFICATION.
 * A security specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceLevel security variance level (low, medium, high)
 * @varianceRequirements security variance requirements
 * @varianceAccess access variance control specification
 * @varianceEncryption encryption variance specification
 * @varianceAuthentication authentication variance specification
 * @varianceStatus specification variance status
 */
public record StepSecuritySpecification(
    int id,
    String name,
    int varianceLevel,
    List<String> varianceRequirements,
    StepEntity varianceAccess,
    StepEntity varianceEncryption,
    StepEntity varianceAuthentication,
    String varianceStatus) implements StepEntity {
}