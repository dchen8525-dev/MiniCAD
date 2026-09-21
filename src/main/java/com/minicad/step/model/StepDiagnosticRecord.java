package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DIAGNOSTIC_RECORD.
 * A diagnostic record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem diagnosed variance system
 * @varianceTests diagnostic variance tests
 * @varianceResults diagnostic variance results
 * @varianceConclusion diagnostic variance conclusion
 * @varianceRecommendation recommendation variance for fix
 * @varianceStatus record variance status
 */
public final class StepDiagnosticRecord extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final List<String> varianceTests;
    private final List<String> varianceResults;
    private final String varianceConclusion;
    private final String varianceRecommendation;
    private final String varianceStatus;

    public StepDiagnosticRecord(int id, String name, StepEntity varianceSystem, List<String> varianceTests, List<String> varianceResults, String varianceConclusion, String varianceRecommendation, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceTests = varianceTests == null ? null : java.util.List.copyOf(varianceTests);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusion = varianceConclusion;
        this.varianceRecommendation = varianceRecommendation;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public List<String> getVarianceTests() {
        return varianceTests;
    }

    public List<String> getVarianceResults() {
        return varianceResults;
    }

    public String getVarianceConclusion() {
        return varianceConclusion;
    }

    public String getVarianceRecommendation() {
        return varianceRecommendation;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceTests", varianceTests);
        state.put("varianceResults", varianceResults);
        state.put("varianceConclusion", varianceConclusion);
        state.put("varianceRecommendation", varianceRecommendation);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
