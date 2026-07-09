package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepInstructionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity instructionDefinition;
    private final String instructionState;
    private final long instructionAddress;
    private final boolean instructionExecuted;
    private final String instructionResult;
    private final String instructionStatus;

    public StepInstructionInstance(int id, String name, StepEntity instructionDefinition, String instructionState, long instructionAddress, boolean instructionExecuted, String instructionResult, String instructionStatus) {
        this.id = id;
        this.name = name;
        this.instructionDefinition = instructionDefinition;
        this.instructionState = instructionState;
        this.instructionAddress = instructionAddress;
        this.instructionExecuted = instructionExecuted;
        this.instructionResult = instructionResult;
        this.instructionStatus = instructionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInstructionInstance that = (StepInstructionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(instructionDefinition, that.instructionDefinition) && Objects.equals(instructionState, that.instructionState) && instructionAddress == that.instructionAddress && instructionExecuted == that.instructionExecuted && Objects.equals(instructionResult, that.instructionResult) && Objects.equals(instructionStatus, that.instructionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructionDefinition, instructionState, instructionAddress, instructionExecuted, instructionResult, instructionStatus);
    }

    @Override
    public String toString() {
        return "StepInstructionInstance{" + "id=" + id + "name=" + name + "instructionDefinition=" + instructionDefinition + "instructionState=" + instructionState + "instructionAddress=" + instructionAddress + "instructionExecuted=" + instructionExecuted + "instructionResult=" + instructionResult + "instructionStatus=" + instructionStatus + "}";
    }
}