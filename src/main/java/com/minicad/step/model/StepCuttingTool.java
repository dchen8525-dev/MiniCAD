package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCuttingTool extends AbstractStepEntity {
    private final String toolType;
    private final double toolDiameter;
    private final double toolLength;
    private final double cuttingLength;
    private final int numberOfFlutes;
    private final StepEntity toolMaterial;
    private final StepEntity toolGeometry;

    public StepCuttingTool(int id, String name, String toolType, double toolDiameter, double toolLength, double cuttingLength, int numberOfFlutes, StepEntity toolMaterial, StepEntity toolGeometry) {
        super(id, name);
        this.toolType = toolType;
        this.toolDiameter = toolDiameter;
        this.toolLength = toolLength;
        this.cuttingLength = cuttingLength;
        this.numberOfFlutes = numberOfFlutes;
        this.toolMaterial = toolMaterial;
        this.toolGeometry = toolGeometry;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("toolType", toolType);
        state.put("toolDiameter", toolDiameter);
        state.put("toolLength", toolLength);
        state.put("cuttingLength", cuttingLength);
        state.put("numberOfFlutes", numberOfFlutes);
        state.put("toolMaterial", toolMaterial);
        state.put("toolGeometry", toolGeometry);
        return state;
    }
}
