package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepToolAssembly implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity cuttingTool;
    private final StepEntity toolHolder;
    private final List<StepEntity> adapter;
    private final double overallLength;
    private final double gaugeLength;
    private final String spindleInterface;

    public StepToolAssembly(int id, String name, StepEntity cuttingTool, StepEntity toolHolder, List<StepEntity> adapter, double overallLength, double gaugeLength, String spindleInterface) {
        this.id = id;
        this.name = name;
        this.cuttingTool = cuttingTool;
        this.toolHolder = toolHolder;
        this.adapter = adapter == null ? null : java.util.List.copyOf(adapter);
        this.overallLength = overallLength;
        this.gaugeLength = gaugeLength;
        this.spindleInterface = spindleInterface;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCuttingTool() {
        return cuttingTool;
    }

    public StepEntity getToolHolder() {
        return toolHolder;
    }

    public List<StepEntity> getAdapter() {
        return adapter;
    }

    public double getOverallLength() {
        return overallLength;
    }

    public double getGaugeLength() {
        return gaugeLength;
    }

    public String getSpindleInterface() {
        return spindleInterface;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToolAssembly that = (StepToolAssembly) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(cuttingTool, that.cuttingTool) && Objects.equals(toolHolder, that.toolHolder) && Objects.equals(adapter, that.adapter) && overallLength == that.overallLength && gaugeLength == that.gaugeLength && Objects.equals(spindleInterface, that.spindleInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cuttingTool, toolHolder, adapter, overallLength, gaugeLength, spindleInterface);
    }

    @Override
    public String toString() {
        return "StepToolAssembly{" + "id=" + id + "name=" + name + "cuttingTool=" + cuttingTool + "toolHolder=" + toolHolder + "adapter=" + adapter + "overallLength=" + overallLength + "gaugeLength=" + gaugeLength + "spindleInterface=" + spindleInterface + "}";
    }
}