package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INSTRUCTION_INSTANCE.
 * An instruction instance entity.
 *
 * @param id STEP instance id
 * @param name instruction instance name
 * @param instructionDefinition instruction variance definition reference
 * @param instructionState instruction variance state
 * @param instructionAddress instruction variance address
 * @param instructionExecuted instruction variance executed flag
 * @param instructionResult instruction variance result
 * @param instructionStatus instruction variance status
 */
public final class StepInstructionInstance extends AbstractStepEntity {
    private final StepEntity instructionDefinition;
    private final String instructionState;
    private final long instructionAddress;
    private final boolean instructionExecuted;
    private final String instructionResult;
    private final String instructionStatus;

    public StepInstructionInstance(int id, String name, StepEntity instructionDefinition, String instructionState, long instructionAddress, boolean instructionExecuted, String instructionResult, String instructionStatus) {
        super(id, name);
        this.instructionDefinition = instructionDefinition;
        this.instructionState = instructionState;
        this.instructionAddress = instructionAddress;
        this.instructionExecuted = instructionExecuted;
        this.instructionResult = instructionResult;
        this.instructionStatus = instructionStatus;
    }

    public StepEntity getInstructionDefinition() {
        return instructionDefinition;
    }

    public String getInstructionState() {
        return instructionState;
    }

    public long getInstructionAddress() {
        return instructionAddress;
    }

    public boolean isInstructionExecuted() {
        return instructionExecuted;
    }

    public String getInstructionResult() {
        return instructionResult;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("instructionDefinition", instructionDefinition);
        state.put("instructionState", instructionState);
        state.put("instructionAddress", instructionAddress);
        state.put("instructionExecuted", instructionExecuted);
        state.put("instructionResult", instructionResult);
        state.put("instructionStatus", instructionStatus);
        return state;
    }
}
