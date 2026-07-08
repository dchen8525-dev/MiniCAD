package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CNC_PROGRAM.
 * A CNC program entity.
 *
 * @param id STEP instance id
 * @param name program name
 * @param programType program type (turning, milling, drilling)
 * @param toolpaths toolpath sequence
 * @param machiningOperations machining operations
 * @param programCode program code/G-code reference
 * @param postProcessor post-processor specification
 * @param machineTarget target CNC machine
 */
/**
 * Resolved CNC_PROGRAM.
 * A CNC program entity.
 *
 * @param id STEP instance id
 * @param name program name
 * @param programType program type (turning, milling, drilling)
 * @param toolpaths toolpath sequence
 * @param machiningOperations machining operations
 * @param programCode program code/G-code reference
 * @param postProcessor post-processor specification
 * @param machineTarget target CNC machine
 */
public final class StepCncProgram implements StepEntity {
    private final int id;
    private final String name;
    private final String programType;
    private final List<StepEntity> toolpaths;
    private final List<StepEntity> machiningOperations;
    private final StepEntity programCode;
    private final String postProcessor;
    private final StepEntity machineTarget;

    public StepCncProgram(int id, String name, String programType, List<StepEntity> toolpaths, List<StepEntity> machiningOperations, StepEntity programCode, String postProcessor, StepEntity machineTarget) {
        this.id = id;
        this.name = name;
        this.programType = programType;
        this.toolpaths = toolpaths == null ? null : java.util.List.copyOf(toolpaths);
        this.machiningOperations = machiningOperations == null ? null : java.util.List.copyOf(machiningOperations);
        this.programCode = programCode;
        this.postProcessor = postProcessor;
        this.machineTarget = machineTarget;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProgramType() {
        return programType;
    }

    public List<StepEntity> getToolpaths() {
        return toolpaths;
    }

    public List<StepEntity> getMachiningOperations() {
        return machiningOperations;
    }

    public StepEntity getProgramCode() {
        return programCode;
    }

    public String getPostProcessor() {
        return postProcessor;
    }

    public StepEntity getMachineTarget() {
        return machineTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCncProgram that = (StepCncProgram) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(programType, that.programType) && Objects.equals(toolpaths, that.toolpaths) && Objects.equals(machiningOperations, that.machiningOperations) && Objects.equals(programCode, that.programCode) && Objects.equals(postProcessor, that.postProcessor) && Objects.equals(machineTarget, that.machineTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, programType, toolpaths, machiningOperations, programCode, postProcessor, machineTarget);
    }

    @Override
    public String toString() {
        return "StepCncProgram{" + "id=" + id + "name=" + name + "programType=" + programType + "toolpaths=" + toolpaths + "machiningOperations=" + machiningOperations + "programCode=" + programCode + "postProcessor=" + postProcessor + "machineTarget=" + machineTarget + "}";
    }
}