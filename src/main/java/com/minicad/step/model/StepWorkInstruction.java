package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepWorkInstruction implements StepEntity {
    private final int id;
    private final String name;
    private final String instructionId;
    private final String instructionType;
    private final List<StepEntity> instructionSteps;
    private final List<StepEntity> instructionMedia;
    private final List<StepEntity> instructionTools;
    private final String instructionStatus;

    public StepWorkInstruction(int id, String name, String instructionId, String instructionType, List<StepEntity> instructionSteps, List<StepEntity> instructionMedia, List<StepEntity> instructionTools, String instructionStatus) {
        this.id = id;
        this.name = name;
        this.instructionId = instructionId;
        this.instructionType = instructionType;
        this.instructionSteps = instructionSteps == null ? null : java.util.List.copyOf(instructionSteps);
        this.instructionMedia = instructionMedia == null ? null : java.util.List.copyOf(instructionMedia);
        this.instructionTools = instructionTools == null ? null : java.util.List.copyOf(instructionTools);
        this.instructionStatus = instructionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWorkInstruction that = (StepWorkInstruction) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(instructionId, that.instructionId) && Objects.equals(instructionType, that.instructionType) && Objects.equals(instructionSteps, that.instructionSteps) && Objects.equals(instructionMedia, that.instructionMedia) && Objects.equals(instructionTools, that.instructionTools) && Objects.equals(instructionStatus, that.instructionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, instructionId, instructionType, instructionSteps, instructionMedia, instructionTools, instructionStatus);
    }

    @Override
    public String toString() {
        return "StepWorkInstruction{" + "id=" + id + "name=" + name + "instructionId=" + instructionId + "instructionType=" + instructionType + "instructionSteps=" + instructionSteps + "instructionMedia=" + instructionMedia + "instructionTools=" + instructionTools + "instructionStatus=" + instructionStatus + "}";
    }
}