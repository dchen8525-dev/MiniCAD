package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACCEPTANCE_TEST_RESULT.
 * An acceptance test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceSystem tested variance system
 * @varianceCriteria acceptance variance criteria
 * @varianceTests acceptance variance test cases
 * @varianceOutcome acceptance variance outcome
 * @varianceSignoff signoff variance reference
 * @varianceStatus result variance status
 */
public final class StepAcceptanceTestResult extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final StepEntity varianceCriteria;
    private final List<StepEntity> varianceTests;
    private final String varianceOutcome;
    private final StepEntity varianceSignoff;
    private final String varianceStatus;

    public StepAcceptanceTestResult(int id, String name, StepEntity varianceSystem, StepEntity varianceCriteria, List<StepEntity> varianceTests, String varianceOutcome, StepEntity varianceSignoff, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceCriteria = varianceCriteria;
        this.varianceTests = varianceTests == null ? null : java.util.List.copyOf(varianceTests);
        this.varianceOutcome = varianceOutcome;
        this.varianceSignoff = varianceSignoff;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public StepEntity getVarianceCriteria() {
        return varianceCriteria;
    }

    public List<StepEntity> getVarianceTests() {
        return varianceTests;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public StepEntity getVarianceSignoff() {
        return varianceSignoff;
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
        state.put("varianceCriteria", varianceCriteria);
        state.put("varianceTests", varianceTests);
        state.put("varianceOutcome", varianceOutcome);
        state.put("varianceSignoff", varianceSignoff);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
