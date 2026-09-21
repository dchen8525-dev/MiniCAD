package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepResolutionRecord extends AbstractStepEntity {
    private final StepEntity varianceIssue;
    private final String varianceSolution;
    private final StepEntity varianceDate;
    private final StepEntity varianceResolver;
    private final String varianceVerification;
    private final List<String> variancePrevention;
    private final String varianceStatus;

    public StepResolutionRecord(int id, String name, StepEntity varianceIssue, String varianceSolution, StepEntity varianceDate, StepEntity varianceResolver, String varianceVerification, List<String> variancePrevention, String varianceStatus) {
        super(id, name);
        this.varianceIssue = varianceIssue;
        this.varianceSolution = varianceSolution;
        this.varianceDate = varianceDate;
        this.varianceResolver = varianceResolver;
        this.varianceVerification = varianceVerification;
        this.variancePrevention = variancePrevention == null ? null : java.util.List.copyOf(variancePrevention);
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceIssue", varianceIssue);
        state.put("varianceSolution", varianceSolution);
        state.put("varianceDate", varianceDate);
        state.put("varianceResolver", varianceResolver);
        state.put("varianceVerification", varianceVerification);
        state.put("variancePrevention", variancePrevention);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
