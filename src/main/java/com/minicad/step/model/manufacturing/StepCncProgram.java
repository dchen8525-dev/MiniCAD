package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepCncProgram(
    int id,
    String name,
    String programType,
    List<StepEntity> toolpaths,
    List<StepEntity> machiningOperations,
    StepEntity programCode,
    String postProcessor,
    StepEntity machineTarget) implements StepEntity {
}