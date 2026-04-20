package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STATE_MACHINE_INSTANCE.
 * A state machine instance entity.
 *
 * @param id STEP instance id
 * @param name state machine instance name
 * @param machineDefinition machine variance definition reference
 * @param machineState machine variance current state reference
 * @param machinePreviousState machine variance previous state reference
 * @param machineTransitionCount machine variance transition count
 * @param machineStatus machine variance status
 */
public record StepStateMachineInstance(
    int id,
    String name,
    StepEntity machineDefinition,
    StepEntity machineState,
    StepEntity machinePreviousState,
    int machineTransitionCount,
    String machineStatus) implements StepEntity {
}