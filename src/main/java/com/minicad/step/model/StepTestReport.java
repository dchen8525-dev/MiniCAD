package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTestReport implements StepEntity {
    private final int id;
    private final String name;
    private final String reportId;
    private final String testType;
    private final List<StepEntity> varianceResults;
    private final String varianceConclusions;
    private final List<String> varianceRecommendations;
    private final StepEntity varianceDate;
    private final String reportStatus;

    public StepTestReport(int id, String name, String reportId, String testType, List<StepEntity> varianceResults, String varianceConclusions, List<String> varianceRecommendations, StepEntity varianceDate, String reportStatus) {
        this.id = id;
        this.name = name;
        this.reportId = reportId;
        this.testType = testType;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusions = varianceConclusions;
        this.varianceRecommendations = varianceRecommendations == null ? null : java.util.List.copyOf(varianceRecommendations);
        this.varianceDate = varianceDate;
        this.reportStatus = reportStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTestReport that = (StepTestReport) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(reportId, that.reportId) && Objects.equals(testType, that.testType) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(varianceConclusions, that.varianceConclusions) && Objects.equals(varianceRecommendations, that.varianceRecommendations) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(reportStatus, that.reportStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reportId, testType, varianceResults, varianceConclusions, varianceRecommendations, varianceDate, reportStatus);
    }

    @Override
    public String toString() {
        return "StepTestReport{" + "id=" + id + "name=" + name + "reportId=" + reportId + "testType=" + testType + "varianceResults=" + varianceResults + "varianceConclusions=" + varianceConclusions + "varianceRecommendations=" + varianceRecommendations + "varianceDate=" + varianceDate + "reportStatus=" + reportStatus + "}";
    }
}