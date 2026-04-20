package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepInstructionInstance(
    int id,
    String name,
    StepEntity instructionDefinition,
    String instructionState,
    long instructionAddress,
    boolean instructionExecuted,
    String instructionResult,
    String instructionStatus) implements StepEntity {
}