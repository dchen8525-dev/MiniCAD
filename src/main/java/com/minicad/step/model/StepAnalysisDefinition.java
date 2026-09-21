package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ANALYSIS_DEFINITION.
 * An analysis definition entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @param analysisType analysis variance type
 * @param analysisMethod analysis variance method
 * @param analysisInputs analysis variance inputs
 * @param analysisOutputs analysis variance expected outputs
 * @param analysisStatus analysis variance status
 */
public final class StepAnalysisDefinition extends AbstractStepEntity {
    private final String analysisType;
    private final String analysisMethod;
    private final List<String> analysisInputs;
    private final List<String> analysisOutputs;
    private final String analysisStatus;

    public StepAnalysisDefinition(int id, String name, String analysisType, String analysisMethod, List<String> analysisInputs, List<String> analysisOutputs, String analysisStatus) {
        super(id, name);
        this.analysisType = analysisType;
        this.analysisMethod = analysisMethod;
        this.analysisInputs = analysisInputs == null ? null : java.util.List.copyOf(analysisInputs);
        this.analysisOutputs = analysisOutputs == null ? null : java.util.List.copyOf(analysisOutputs);
        this.analysisStatus = analysisStatus;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    public String getAnalysisMethod() {
        return analysisMethod;
    }

    public List<String> getAnalysisInputs() {
        return analysisInputs;
    }

    public List<String> getAnalysisOutputs() {
        return analysisOutputs;
    }

    public String getAnalysisStatus() {
        return analysisStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("analysisType", analysisType);
        state.put("analysisMethod", analysisMethod);
        state.put("analysisInputs", analysisInputs);
        state.put("analysisOutputs", analysisOutputs);
        state.put("analysisStatus", analysisStatus);
        return state;
    }
}
