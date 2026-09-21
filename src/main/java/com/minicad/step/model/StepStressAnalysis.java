package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved STRESS_ANALYSIS.
 * Stress analysis type for FEA.
 */
public final class StepStressAnalysis extends AbstractStepEntity {
    private final String analysisType;

    public StepStressAnalysis(int id, String name, String analysisType) {
        super(id, name);
        this.analysisType = analysisType;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("analysisType", analysisType);
        return state;
    }
}
