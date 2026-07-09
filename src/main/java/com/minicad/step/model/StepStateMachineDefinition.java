package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStateMachineDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String machineType;
    private final List<StepEntity> machineStates;
    private final List<StepEntity> machineTransitions;
    private final StepEntity machineInitialState;
    private final String machineStatus;

    public StepStateMachineDefinition(int id, String name, String machineType, List<StepEntity> machineStates, List<StepEntity> machineTransitions, StepEntity machineInitialState, String machineStatus) {
        this.id = id;
        this.name = name;
        this.machineType = machineType;
        this.machineStates = machineStates == null ? null : java.util.List.copyOf(machineStates);
        this.machineTransitions = machineTransitions == null ? null : java.util.List.copyOf(machineTransitions);
        this.machineInitialState = machineInitialState;
        this.machineStatus = machineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMachineType() {
        return machineType;
    }

    public List<StepEntity> getMachineStates() {
        return machineStates;
    }

    public List<StepEntity> getMachineTransitions() {
        return machineTransitions;
    }

    public StepEntity getMachineInitialState() {
        return machineInitialState;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStateMachineDefinition that = (StepStateMachineDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(machineType, that.machineType) && Objects.equals(machineStates, that.machineStates) && Objects.equals(machineTransitions, that.machineTransitions) && Objects.equals(machineInitialState, that.machineInitialState) && Objects.equals(machineStatus, that.machineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, machineType, machineStates, machineTransitions, machineInitialState, machineStatus);
    }

    @Override
    public String toString() {
        return "StepStateMachineDefinition{" + "id=" + id + "name=" + name + "machineType=" + machineType + "machineStates=" + machineStates + "machineTransitions=" + machineTransitions + "machineInitialState=" + machineInitialState + "machineStatus=" + machineStatus + "}";
    }
}