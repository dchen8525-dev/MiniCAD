package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ANALYSIS_INSTANCE.
 * An analysis instance entity.
 *
 * @param id STEP instance id
 * @param name analysis instance name
 * @param analysisDefinition analysis variance definition reference
 * @param analysisState analysis variance state
 * @param analysisResults analysis variance results
 * @param analysisConclusions analysis variance conclusions
 * @param analysisStatus analysis variance status
 */
public final class StepAnalysisInstance extends AbstractStepEntity {
    private final StepEntity analysisDefinition;
    private final String analysisState;
    private final List<String> analysisResults;
    private final String analysisConclusions;
    private final String analysisStatus;

    public StepAnalysisInstance(int id, String name, StepEntity analysisDefinition, String analysisState, List<String> analysisResults, String analysisConclusions, String analysisStatus) {
        super(id, name);
        this.analysisDefinition = analysisDefinition;
        this.analysisState = analysisState;
        this.analysisResults = analysisResults == null ? null : java.util.List.copyOf(analysisResults);
        this.analysisConclusions = analysisConclusions;
        this.analysisStatus = analysisStatus;
    }

    public StepEntity getAnalysisDefinition() {
        return analysisDefinition;
    }

    public String getAnalysisState() {
        return analysisState;
    }

    public List<String> getAnalysisResults() {
        return analysisResults;
    }

    public String getAnalysisConclusions() {
        return analysisConclusions;
    }

    public String getAnalysisStatus() {
        return analysisStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("analysisDefinition", analysisDefinition);
        state.put("analysisState", analysisState);
        state.put("analysisResults", analysisResults);
        state.put("analysisConclusions", analysisConclusions);
        state.put("analysisStatus", analysisStatus);
        return state;
    }
}
