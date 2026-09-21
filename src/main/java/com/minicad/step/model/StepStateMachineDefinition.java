package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStateMachineDefinition extends AbstractStepEntity {
    private final String machineType;
    private final List<StepEntity> machineStates;
    private final List<StepEntity> machineTransitions;
    private final StepEntity machineInitialState;
    private final String machineStatus;

    public StepStateMachineDefinition(int id, String name, String machineType, List<StepEntity> machineStates, List<StepEntity> machineTransitions, StepEntity machineInitialState, String machineStatus) {
        super(id, name);
        this.machineType = machineType;
        this.machineStates = machineStates == null ? null : java.util.List.copyOf(machineStates);
        this.machineTransitions = machineTransitions == null ? null : java.util.List.copyOf(machineTransitions);
        this.machineInitialState = machineInitialState;
        this.machineStatus = machineStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("machineType", machineType);
        state.put("machineStates", machineStates);
        state.put("machineTransitions", machineTransitions);
        state.put("machineInitialState", machineInitialState);
        state.put("machineStatus", machineStatus);
        return state;
    }
}
