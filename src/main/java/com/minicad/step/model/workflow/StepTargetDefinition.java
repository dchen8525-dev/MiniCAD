package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TARGET_DEFINITION.
 * A target definition entity.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetType target variance type
 * @param targetValue target variance value
 * @param targetUnit target variance unit
 * @param targetDeadline target variance deadline
 * @param targetPriority target variance priority
 * @param targetStatus target variance status
 */
public record StepTargetDefinition(
    int id,
    String name,
    String targetType,
    double targetValue,
    StepEntity targetUnit,
    StepEntity targetDeadline,
    int targetPriority,
    String targetStatus) implements StepEntity {
}