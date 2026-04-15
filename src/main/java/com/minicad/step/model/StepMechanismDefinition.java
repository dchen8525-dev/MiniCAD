package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MECHANISM_DEFINITION.
 * A mechanism definition entity.
 *
 * @param id STEP instance id
 * @param name mechanism name
 * @param mechanismType mechanism type classification
 * @param links mechanism links/parts
 * @param joints mechanism joints connecting links
 * @param degreesOfFreedom degrees of freedom count
 * @param baseLink base/grounded link
 * @param actuatedJoints actuated joints list
 */
public record StepMechanismDefinition(
    int id,
    String name,
    String mechanismType,
    List<StepEntity> links,
    List<StepEntity> joints,
    int degreesOfFreedom,
    StepEntity baseLink,
    List<StepEntity> actuatedJoints) implements StepEntity {
}