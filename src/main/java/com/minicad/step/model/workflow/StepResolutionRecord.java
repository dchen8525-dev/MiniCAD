package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESOLUTION_RECORD.
 * A resolution record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceIssue resolved variance issue
 * @varianceSolution solution variance applied
 * @varianceDate resolution variance date
 * @varianceResolver resolver variance reference
 * @varianceVerification verification variance method
 * @variancePrevention prevention variance measures
 * @varianceStatus record variance status
 */
/**
 * Resolved RESOLUTION_RECORD.
 * A resolution record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceIssue resolved variance issue
 * @varianceSolution solution variance applied
 * @varianceDate resolution variance date
 * @varianceResolver resolver variance reference
 * @varianceVerification verification variance method
 * @variancePrevention prevention variance measures
 * @varianceStatus record variance status
 */
public final class StepResolutionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceIssue;
    private final String varianceSolution;
    private final StepEntity varianceDate;
    private final StepEntity varianceResolver;
    private final String varianceVerification;
    private final List<String> variancePrevention;
    private final String varianceStatus;

    public StepResolutionRecord(int id, String name, StepEntity varianceIssue, String varianceSolution, StepEntity varianceDate, StepEntity varianceResolver, String varianceVerification, List<String> variancePrevention, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceIssue = varianceIssue;
        this.varianceSolution = varianceSolution;
        this.varianceDate = varianceDate;
        this.varianceResolver = varianceResolver;
        this.varianceVerification = varianceVerification;
        this.variancePrevention = variancePrevention == null ? null : java.util.List.copyOf(variancePrevention);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceIssue() {
        return varianceIssue;
    }

    public String getVarianceSolution() {
        return varianceSolution;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceResolver() {
        return varianceResolver;
    }

    public String getVarianceVerification() {
        return varianceVerification;
    }

    public List<String> getVariancePrevention() {
        return variancePrevention;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResolutionRecord that = (StepResolutionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceIssue, that.varianceIssue) && Objects.equals(varianceSolution, that.varianceSolution) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceResolver, that.varianceResolver) && Objects.equals(varianceVerification, that.varianceVerification) && Objects.equals(variancePrevention, that.variancePrevention) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceIssue, varianceSolution, varianceDate, varianceResolver, varianceVerification, variancePrevention, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepResolutionRecord{" + "id=" + id + "name=" + name + "varianceIssue=" + varianceIssue + "varianceSolution=" + varianceSolution + "varianceDate=" + varianceDate + "varianceResolver=" + varianceResolver + "varianceVerification=" + varianceVerification + "variancePrevention=" + variancePrevention + "varianceStatus=" + varianceStatus + "}";
    }
}