package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepToolAssembly extends AbstractStepEntity {
    private final StepEntity cuttingTool;
    private final StepEntity toolHolder;
    private final List<StepEntity> adapter;
    private final double overallLength;
    private final double gaugeLength;
    private final String spindleInterface;

    public StepToolAssembly(int id, String name, StepEntity cuttingTool, StepEntity toolHolder, List<StepEntity> adapter, double overallLength, double gaugeLength, String spindleInterface) {
        super(id, name);
        this.cuttingTool = cuttingTool;
        this.toolHolder = toolHolder;
        this.adapter = adapter == null ? null : java.util.List.copyOf(adapter);
        this.overallLength = overallLength;
        this.gaugeLength = gaugeLength;
        this.spindleInterface = spindleInterface;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("cuttingTool", cuttingTool);
        state.put("toolHolder", toolHolder);
        state.put("adapter", adapter);
        state.put("overallLength", overallLength);
        state.put("gaugeLength", gaugeLength);
        state.put("spindleInterface", spindleInterface);
        return state;
    }
}
