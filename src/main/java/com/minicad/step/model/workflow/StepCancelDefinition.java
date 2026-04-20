package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CANCEL_DEFINITION.
 * A cancel definition entity.
 *
 * @param id STEP instance id
 * @param name cancel name
 * @param cancelType cancel variance type
 * @param cancelCondition cancel variance condition
 * @param cancelGracePeriod cancel variance grace period
 * @param cancelCleanup cancel variance cleanup action
 * @param cancelStatus cancel variance status
 */
public record StepCancelDefinition(
    int id,
    String name,
    String cancelType,
    String cancelCondition,
    int cancelGracePeriod,
    StepEntity cancelCleanup,
    String cancelStatus) implements StepEntity {
}