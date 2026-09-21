package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStateMachineInstance extends AbstractStepEntity {
    private final StepEntity machineDefinition;
    private final StepEntity machineState;
    private final StepEntity machinePreviousState;
    private final int machineTransitionCount;
    private final String machineStatus;

    public StepStateMachineInstance(int id, String name, StepEntity machineDefinition, StepEntity machineState, StepEntity machinePreviousState, int machineTransitionCount, String machineStatus) {
        super(id, name);
        this.machineDefinition = machineDefinition;
        this.machineState = machineState;
        this.machinePreviousState = machinePreviousState;
        this.machineTransitionCount = machineTransitionCount;
        this.machineStatus = machineStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("machineDefinition", machineDefinition);
        state.put("machineState", machineState);
        state.put("machinePreviousState", machinePreviousState);
        state.put("machineTransitionCount", machineTransitionCount);
        state.put("machineStatus", machineStatus);
        return state;
    }
}
