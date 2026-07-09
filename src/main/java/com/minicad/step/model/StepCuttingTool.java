package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCuttingTool implements StepEntity {
    private final int id;
    private final String name;
    private final String toolType;
    private final double toolDiameter;
    private final double toolLength;
    private final double cuttingLength;
    private final int numberOfFlutes;
    private final StepEntity toolMaterial;
    private final StepEntity toolGeometry;

    public StepCuttingTool(int id, String name, String toolType, double toolDiameter, double toolLength, double cuttingLength, int numberOfFlutes, StepEntity toolMaterial, StepEntity toolGeometry) {
        this.id = id;
        this.name = name;
        this.toolType = toolType;
        this.toolDiameter = toolDiameter;
        this.toolLength = toolLength;
        this.cuttingLength = cuttingLength;
        this.numberOfFlutes = numberOfFlutes;
        this.toolMaterial = toolMaterial;
        this.toolGeometry = toolGeometry;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getToolType() {
        return toolType;
    }

    public double getToolDiameter() {
        return toolDiameter;
    }

    public double getToolLength() {
        return toolLength;
    }

    public double getCuttingLength() {
        return cuttingLength;
    }

    public int getNumberOfFlutes() {
        return numberOfFlutes;
    }

    public StepEntity getToolMaterial() {
        return toolMaterial;
    }

    public StepEntity getToolGeometry() {
        return toolGeometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCuttingTool that = (StepCuttingTool) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(toolType, that.toolType) && toolDiameter == that.toolDiameter && toolLength == that.toolLength && cuttingLength == that.cuttingLength && numberOfFlutes == that.numberOfFlutes && Objects.equals(toolMaterial, that.toolMaterial) && Objects.equals(toolGeometry, that.toolGeometry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, toolType, toolDiameter, toolLength, cuttingLength, numberOfFlutes, toolMaterial, toolGeometry);
    }

    @Override
    public String toString() {
        return "StepCuttingTool{" + "id=" + id + "name=" + name + "toolType=" + toolType + "toolDiameter=" + toolDiameter + "toolLength=" + toolLength + "cuttingLength=" + cuttingLength + "numberOfFlutes=" + numberOfFlutes + "toolMaterial=" + toolMaterial + "toolGeometry=" + toolGeometry + "}";
    }
}