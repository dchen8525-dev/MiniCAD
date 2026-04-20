package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TIMEOUT_INSTANCE.
 * A timeout instance entity.
 *
 * @param id STEP instance id
 * @param name timeout instance name
 * @param timeoutDefinition timeout variance definition reference
 * @param timeoutState timeout variance state
 * @param timeoutStartTime timeout variance start time
 * @param timeoutTriggered timeout variance triggered flag
 * @param timeoutStatus timeout variance status
 */
public record StepTimeoutInstance(
    int id,
    String name,
    StepEntity timeoutDefinition,
    String timeoutState,
    StepEntity timeoutStartTime,
    boolean timeoutTriggered,
    String timeoutStatus) implements StepEntity {
}