package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TOOL_ASSEMBLY.
 * A tool assembly entity.
 *
 * @param id STEP instance id
 * @param name assembly name
 * @param cuttingTool cutting tool component
 * @param toolHolder tool holder component
 * @param adapter adapter components if present
 * @param overallLength overall assembly length
 * @param gaugeLength gauge length from spindle face
 * @param spindleInterface spindle interface type
 */
public record StepToolAssembly(
    int id,
    String name,
    StepEntity cuttingTool,
    StepEntity toolHolder,
    List<StepEntity> adapter,
    double overallLength,
    double gaugeLength,
    String spindleInterface) implements StepEntity {
}