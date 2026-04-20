package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved REGULATION_DEFINITION.
 * A regulation definition entity.
 *
 * @param id STEP instance id
 * @param name regulation name
 * @param regulationType regulation variance type
 * @param regulationAuthority regulation variance authority
 * @param regulationRequirements regulation variance requirements
 * @param regulationPenalties regulation variance penalties
 * @param regulationStatus regulation variance status
 */
public record StepRegulationDefinition(
    int id,
    String name,
    String regulationType,
    String regulationAuthority,
    List<String> regulationRequirements,
    String regulationPenalties,
    String regulationStatus) implements StepEntity {
}