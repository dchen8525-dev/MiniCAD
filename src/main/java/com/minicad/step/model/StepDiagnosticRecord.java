package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDiagnosticRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final List<String> varianceTests;
    private final List<String> varianceResults;
    private final String varianceConclusion;
    private final String varianceRecommendation;
    private final String varianceStatus;

    public StepDiagnosticRecord(int id, String name, StepEntity varianceSystem, List<String> varianceTests, List<String> varianceResults, String varianceConclusion, String varianceRecommendation, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceTests = varianceTests == null ? null : java.util.List.copyOf(varianceTests);
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusion = varianceConclusion;
        this.varianceRecommendation = varianceRecommendation;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDiagnosticRecord that = (StepDiagnosticRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceTests, that.varianceTests) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(varianceConclusion, that.varianceConclusion) && Objects.equals(varianceRecommendation, that.varianceRecommendation) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceTests, varianceResults, varianceConclusion, varianceRecommendation, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDiagnosticRecord{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceTests=" + varianceTests + "varianceResults=" + varianceResults + "varianceConclusion=" + varianceConclusion + "varianceRecommendation=" + varianceRecommendation + "varianceStatus=" + varianceStatus + "}";
    }
}