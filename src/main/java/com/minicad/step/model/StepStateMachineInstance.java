package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStateMachineInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity machineDefinition;
    private final StepEntity machineState;
    private final StepEntity machinePreviousState;
    private final int machineTransitionCount;
    private final String machineStatus;

    public StepStateMachineInstance(int id, String name, StepEntity machineDefinition, StepEntity machineState, StepEntity machinePreviousState, int machineTransitionCount, String machineStatus) {
        this.id = id;
        this.name = name;
        this.machineDefinition = machineDefinition;
        this.machineState = machineState;
        this.machinePreviousState = machinePreviousState;
        this.machineTransitionCount = machineTransitionCount;
        this.machineStatus = machineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getMachineDefinition() {
        return machineDefinition;
    }

    public StepEntity getMachineState() {
        return machineState;
    }

    public StepEntity getMachinePreviousState() {
        return machinePreviousState;
    }

    public int getMachineTransitionCount() {
        return machineTransitionCount;
    }

    public String getMachineStatus() {
        return machineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStateMachineInstance that = (StepStateMachineInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(machineDefinition, that.machineDefinition) && Objects.equals(machineState, that.machineState) && Objects.equals(machinePreviousState, that.machinePreviousState) && machineTransitionCount == that.machineTransitionCount && Objects.equals(machineStatus, that.machineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, machineDefinition, machineState, machinePreviousState, machineTransitionCount, machineStatus);
    }

    @Override
    public String toString() {
        return "StepStateMachineInstance{" + "id=" + id + "name=" + name + "machineDefinition=" + machineDefinition + "machineState=" + machineState + "machinePreviousState=" + machinePreviousState + "machineTransitionCount=" + machineTransitionCount + "machineStatus=" + machineStatus + "}";
    }
}