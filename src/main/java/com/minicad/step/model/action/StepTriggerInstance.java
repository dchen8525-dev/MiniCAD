package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRIGGER_INSTANCE.
 * A trigger instance entity.
 *
 * @param id STEP instance id
 * @param name trigger instance name
 * @param triggerDefinition trigger variance definition reference
 * @param triggerState trigger variance state
 * @param triggerFireCount trigger variance fire count
 * @param triggerLastFire trigger variance last fire time
 * @param triggerStatus trigger variance status
 */
public record StepTriggerInstance(
    int id,
    String name,
    StepEntity triggerDefinition,
    String triggerState,
    int triggerFireCount,
    StepEntity triggerLastFire,
    String triggerStatus) implements StepEntity {
}