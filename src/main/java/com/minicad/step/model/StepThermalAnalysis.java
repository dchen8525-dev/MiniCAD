package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved THERMAL_ANALYSIS.
 * Thermal analysis type for FEA.
 */
public final class StepThermalAnalysis extends AbstractStepEntity {
    private final String thermalAnalysisType;

    public StepThermalAnalysis(int id, String name, String thermalAnalysisType) {
        super(id, name);
        this.thermalAnalysisType = thermalAnalysisType;
    }

    public String getThermalAnalysisType() {
        return thermalAnalysisType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("thermalAnalysisType", thermalAnalysisType);
        return state;
    }
}
