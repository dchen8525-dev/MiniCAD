package com.minicad.step.model.profile_analysis.analysis;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DATA_ANALYSIS.
 * A data analysis entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @varianceData analyzed variance data reference
 * @varianceMethod analysis variance method
 * @varianceResults analysis variance results
 * @varianceConclusions analysis variance conclusions
 * @varianceDate analysis variance date
 * @varianceStatus analysis variance status
 */
/**
 * Resolved DATA_ANALYSIS.
 * A data analysis entity.
 *
 * @param id STEP instance id
 * @param name analysis name
 * @varianceData analyzed variance data reference
 * @varianceMethod analysis variance method
 * @varianceResults analysis variance results
 * @varianceConclusions analysis variance conclusions
 * @varianceDate analysis variance date
 * @varianceStatus analysis variance status
 */
public final class StepDataAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final String varianceMethod;
    private final List<Double> varianceResults;
    private final String varianceConclusions;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepDataAnalysis(int id, String name, StepEntity varianceData, String varianceMethod, List<Double> varianceResults, String varianceConclusions, StepEntity varianceDate, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceMethod = varianceMethod;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusions = varianceConclusions;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public List<Double> getVarianceResults() {
        return varianceResults;
    }

    public String getVarianceConclusions() {
        return varianceConclusions;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataAnalysis that = (StepDataAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceResults, that.varianceResults) && Objects.equals(varianceConclusions, that.varianceConclusions) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceMethod, varianceResults, varianceConclusions, varianceDate, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDataAnalysis{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceMethod=" + varianceMethod + "varianceResults=" + varianceResults + "varianceConclusions=" + varianceConclusions + "varianceDate=" + varianceDate + "varianceStatus=" + varianceStatus + "}";
    }
}