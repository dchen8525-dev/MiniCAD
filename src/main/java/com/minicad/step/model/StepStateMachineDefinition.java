package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STATE_MACHINE_DEFINITION.
 * A state machine definition entity.
 *
 * @param id STEP instance id
 * @param name state machine name
 * @param machineType machine variance type
 * @param machineStates machine variance state definitions
 * @param machineTransitions machine variance transition definitions
 * @param machineInitialState machine variance initial state reference
 * @param machineStatus machine variance status
 */
public record StepStateMachineDefinition(
    int id,
    String name,
    String machineType,
    List<StepEntity> machineStates,
    List<StepEntity> machineTransitions,
    StepEntity machineInitialState,
    String machineStatus) implements StepEntity {
}