package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepInstructionDefinition extends AbstractStepEntity {
    private final String instructionType;
    private final String instructionOpcode;
    private final List<String> instructionOperands;
    private final String instructionDescription;
    private final String instructionStatus;

    public StepInstructionDefinition(int id, String name, String instructionType, String instructionOpcode, List<String> instructionOperands, String instructionDescription, String instructionStatus) {
        super(id, name);
        this.instructionType = instructionType;
        this.instructionOpcode = instructionOpcode;
        this.instructionOperands = instructionOperands == null ? null : java.util.List.copyOf(instructionOperands);
        this.instructionDescription = instructionDescription;
        this.instructionStatus = instructionStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("instructionType", instructionType);
        state.put("instructionOpcode", instructionOpcode);
        state.put("instructionOperands", instructionOperands);
        state.put("instructionDescription", instructionDescription);
        state.put("instructionStatus", instructionStatus);
        return state;
    }
}
