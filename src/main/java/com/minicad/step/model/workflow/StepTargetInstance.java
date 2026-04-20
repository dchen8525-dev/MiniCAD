package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TARGET_INSTANCE.
 * A target instance entity.
 *
 * @param id STEP instance id
 * @param name target instance name
 * @param targetDefinition target variance definition reference
 * @param targetCurrentValue target variance current value
 * @param targetProgress target variance progress percentage
 * @param targetStatus target variance status
 */
public record StepTargetInstance(
    int id,
    String name,
    StepEntity targetDefinition,
    double targetCurrentValue,
    double targetProgress,
    String targetStatus) implements StepEntity {
}