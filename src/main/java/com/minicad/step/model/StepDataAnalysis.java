package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepDataAnalysis extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final String varianceMethod;
    private final List<Double> varianceResults;
    private final String varianceConclusions;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepDataAnalysis(int id, String name, StepEntity varianceData, String varianceMethod, List<Double> varianceResults, String varianceConclusions, StepEntity varianceDate, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceMethod = varianceMethod;
        this.varianceResults = varianceResults == null ? null : java.util.List.copyOf(varianceResults);
        this.varianceConclusions = varianceConclusions;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceData", varianceData,
                "varianceMethod", varianceMethod, "varianceResults", varianceResults,
                "varianceConclusions", varianceConclusions, "varianceDate", varianceDate,
                "varianceStatus", varianceStatus);
    }
}
