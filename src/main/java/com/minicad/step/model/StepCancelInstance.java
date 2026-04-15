package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CANCEL_INSTANCE.
 * A cancel instance entity.
 *
 * @param id STEP instance id
 * @param name cancel instance name
 * @param cancelDefinition cancel variance definition reference
 * @param cancelState cancel variance state
 * @param cancelTime cancel variance cancellation time
 * @param cancelReason cancel variance reason
 * @param cancelStatus cancel variance status
 */
public record StepCancelInstance(
    int id,
    String name,
    StepEntity cancelDefinition,
    String cancelState,
    StepEntity cancelTime,
    String cancelReason,
    String cancelStatus) implements StepEntity {
}