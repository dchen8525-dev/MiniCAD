package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INSTRUCTION_DEFINITION.
 * An instruction definition entity.
 *
 * @param id STEP instance id
 * @param name instruction name
 * @param instructionType instruction variance type
 * @param instructionOpcode instruction variance opcode
 * @param instructionOperands instruction variance operands
 * @param instructionDescription instruction variance description
 * @param instructionStatus instruction variance status
 */
public record StepInstructionDefinition(
    int id,
    String name,
    String instructionType,
    String instructionOpcode,
    List<String> instructionOperands,
    String instructionDescription,
    String instructionStatus) implements StepEntity {
}