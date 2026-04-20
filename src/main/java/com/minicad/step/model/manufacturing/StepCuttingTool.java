package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CUTTING_TOOL.
 * A cutting tool entity.
 *
 * @param id STEP instance id
 * @param name tool name
 * @param toolType cutting tool type (end mill, drill, turning tool)
 * @param toolDiameter tool diameter
 * @param toolLength tool overall length
 * @param cuttingLength cutting edge length
 * @param numberOfFlutes number of flutes/cutting edges
 * @param toolMaterial tool material specification
 * @param toolGeometry tool geometry representation
 */
public record StepCuttingTool(
    int id,
    String name,
    String toolType,
    double toolDiameter,
    double toolLength,
    double cuttingLength,
    int numberOfFlutes,
    StepEntity toolMaterial,
    StepEntity toolGeometry) implements StepEntity {
}