package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepWorkInstruction extends AbstractStepEntity {
    private final String instructionId;
    private final String instructionType;
    private final List<StepEntity> instructionSteps;
    private final List<StepEntity> instructionMedia;
    private final List<StepEntity> instructionTools;
    private final String instructionStatus;

    public StepWorkInstruction(int id, String name, String instructionId, String instructionType, List<StepEntity> instructionSteps, List<StepEntity> instructionMedia, List<StepEntity> instructionTools, String instructionStatus) {
        super(id, name);
        this.instructionId = instructionId;
        this.instructionType = instructionType;
        this.instructionSteps = instructionSteps == null ? null : java.util.List.copyOf(instructionSteps);
        this.instructionMedia = instructionMedia == null ? null : java.util.List.copyOf(instructionMedia);
        this.instructionTools = instructionTools == null ? null : java.util.List.copyOf(instructionTools);
        this.instructionStatus = instructionStatus;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public List<StepEntity> getInstructionSteps() {
        return instructionSteps;
    }

    public List<StepEntity> getInstructionMedia() {
        return instructionMedia;
    }

    public List<StepEntity> getInstructionTools() {
        return instructionTools;
    }

    public String getInstructionStatus() {
        return instructionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("instructionId", instructionId);
        state.put("instructionType", instructionType);
        state.put("instructionSteps", instructionSteps);
        state.put("instructionMedia", instructionMedia);
        state.put("instructionTools", instructionTools);
        state.put("instructionStatus", instructionStatus);
        return state;
    }
}
