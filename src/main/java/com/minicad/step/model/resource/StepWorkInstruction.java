package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved WORK_INSTRUCTION.
 * A work instruction entity.
 *
 * @param id STEP instance id
 * @param name instruction name
 * @param instructionId instruction identifier
 * @param instructionType instruction type (assembly, machining, inspection)
 * @param instructionSteps work instruction steps
 * @param instructionMedia associated media/images
 * @param instructionTools required tools reference
 * @param instructionStatus instruction status (approved, draft)
 */
public record StepWorkInstruction(
    int id,
    String name,
    String instructionId,
    String instructionType,
    List<StepEntity> instructionSteps,
    List<StepEntity> instructionMedia,
    List<StepEntity> instructionTools,
    String instructionStatus) implements StepEntity {
}