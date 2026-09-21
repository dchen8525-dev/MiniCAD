package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved STRUCTURAL_ANALYSIS_REPRESENTATION_PARAMETERS.
 * Parameters for structural analysis representation.
 */
public final class StepStructuralAnalysisRepresentationParameters extends AbstractStepEntity {
    private final String parameterType;

    public StepStructuralAnalysisRepresentationParameters(int id, String name, String parameterType) {
        super(id, name);
        this.parameterType = parameterType;
    }

    public String getParameterType() {
        return parameterType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parameterType", parameterType);
        return state;
    }
}
