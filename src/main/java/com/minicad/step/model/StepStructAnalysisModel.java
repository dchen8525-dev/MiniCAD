package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved STRUCT_ANALYSIS_MODEL.
 * A structural analysis model (AP209).
 */
public final class StepStructAnalysisModel extends AbstractStepEntity {
    private final String analysisType;

    public StepStructAnalysisModel(int id, String name, String analysisType) {
        super(id, name);
        this.analysisType = analysisType;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "analysisType", analysisType);
    }
}
