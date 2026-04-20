package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CALLBACK_INSTANCE.
 * A callback instance entity.
 *
 * @param id STEP instance id
 * @param name callback instance name
 * @param callbackDefinition callback variance definition reference
 * @param callbackState callback variance state
 * @param callbackResult callback variance result
 * @param callbackExecuted callback variance executed flag
 * @param callbackStatus callback variance status
 */
public record StepCallbackInstance(
    int id,
    String name,
    StepEntity callbackDefinition,
    String callbackState,
    String callbackResult,
    boolean callbackExecuted,
    String callbackStatus) implements StepEntity {
}