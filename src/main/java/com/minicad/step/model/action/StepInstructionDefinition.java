package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepInstructionDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String instructionType;
    private final String instructionOpcode;
    private final List<String> instructionOperands;
    private final String instructionDescription;
    private final String instructionStatus;

    public StepInstructionDefinition(int id, String name, String instructionType, String instructionOpcode, List<String> instructionOperands, String instructionDescription, String instructionStatus) {
        this.id = id;
        this.name = name;
        this.instructionType = instructionType;
        this.instructionOpcode = instructionOpcode;
        this.instructionOperands = instructionOperands == null ? null : java.util.List.copyOf(instructionOperands);
        this.instructionDescription = instructionDescription;
        this.instructionStatus = instructionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public String getInstructionOpcode() {
        return instructionOpcode;
    }

    public List<String> getInstructionOperands() {
        return instructionOperands;
    }

    public String getInstructionDescription() {
        return instructionDescription;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInstructionDefinition that = (StepInstructionDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(instructionType, that.instructionType) && Objects.equals(instructionOpcode, that.instructionOpcode) && Objects.equals(instructionOperands, that.instructionOperands) && Objects.equals(instructionDescription, that.instructionDescription) && Objects.equals(instructionStatus, that.instructionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructionType, instructionOpcode, instructionOperands, instructionDescription, instructionStatus);
    }

    @Override
    public String toString() {
        return "StepInstructionDefinition{" + "id=" + id + "name=" + name + "instructionType=" + instructionType + "instructionOpcode=" + instructionOpcode + "instructionOperands=" + instructionOperands + "instructionDescription=" + instructionDescription + "instructionStatus=" + instructionStatus + "}";
    }
}