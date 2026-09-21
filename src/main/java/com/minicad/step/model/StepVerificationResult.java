package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved VERIFICATION_RESULT.
 * A verification result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem verified variance item
 * @varianceMethod verification variance method
 * @varianceCriteria verification variance criteria
 * @varianceOutcome verification variance outcome (pass/fail)
 * @varianceEvidence evidence variance reference
 * @varianceStatus result variance status
 */
public final class StepVerificationResult extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final StepEntity varianceCriteria;
    private final String varianceOutcome;
    private final StepEntity varianceEvidence;
    private final String varianceStatus;

    public StepVerificationResult(int id, String name, StepEntity varianceItem, String varianceMethod, StepEntity varianceCriteria, String varianceOutcome, StepEntity varianceEvidence, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceCriteria = varianceCriteria;
        this.varianceOutcome = varianceOutcome;
        this.varianceEvidence = varianceEvidence;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceCriteria() {
        return varianceCriteria;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public StepEntity getVarianceEvidence() {
        return varianceEvidence;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceMethod", varianceMethod);
        state.put("varianceCriteria", varianceCriteria);
        state.put("varianceOutcome", varianceOutcome);
        state.put("varianceEvidence", varianceEvidence);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
