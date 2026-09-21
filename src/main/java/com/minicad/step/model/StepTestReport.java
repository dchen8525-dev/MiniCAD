package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TEST_REPORT.
 * A test report entity.
 *
 * @param id STEP instance id
 * @param name report name
 * @param reportId report identifier
 * @param testType test type (functional, performance, environmental)
 * @varianceResults test variance results
 * @varianceConclusions test variance conclusions
 * @varianceRecommendations test variance recommendations
 * @varianceDate test variance date
 * @param reportStatus report status
 */
public final class StepTestReport extends AbstractStepEntity {
    private final String reportId;
    private final String testType;
    private final List<StepEntity> varianceResults;
    private final String varianceConclusions;
    private final List<String> varianceRecommendations;
    private final StepEntity varianceDate;
    private final String reportStatus;

    public StepTestReport(int id, String name, String reportId, String testType, List<StepEntity> varianceResults, String varianceConclusions, List<String> varianceRecommendations, StepEntity varianceDate, String reportStatus) {
        super(id, name);
        this.reportId = reportId;
        this.testType = testType;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusions = varianceConclusions;
        this.varianceRecommendations = varianceRecommendations == null ? null : java.util.List.copyOf(varianceRecommendations);
        this.varianceDate = varianceDate;
        this.reportStatus = reportStatus;
    }

    public String getReportId() {
        return reportId;
    }

    public String getTestType() {
        return testType;
    }

    public List<StepEntity> getVarianceResults() {
        return varianceResults;
    }

    public String getVarianceConclusions() {
        return varianceConclusions;
    }

    public List<String> getVarianceRecommendations() {
        return varianceRecommendations;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("reportId", reportId);
        state.put("testType", testType);
        state.put("varianceResults", varianceResults);
        state.put("varianceConclusions", varianceConclusions);
        state.put("varianceRecommendations", varianceRecommendations);
        state.put("varianceDate", varianceDate);
        state.put("reportStatus", reportStatus);
        return state;
    }
}
