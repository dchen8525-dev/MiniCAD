package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INTEGRATION_TEST_RESULT.
 * An integration test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceComponents integrated variance components
 * @varianceInterfaces tested variance interfaces
 * @varianceIssues integration variance issues
 * @varianceResolved resolved variance issues count
 * @varianceStatus result variance status
 */
public final class StepIntegrationTestResult extends AbstractStepEntity {
    private final List<StepEntity> varianceComponents;
    private final List<StepEntity> varianceInterfaces;
    private final List<String> varianceIssues;
    private final int varianceResolved;
    private final String varianceStatus;

    public StepIntegrationTestResult(int id, String name, List<StepEntity> varianceComponents, List<StepEntity> varianceInterfaces, List<String> varianceIssues, int varianceResolved, String varianceStatus) {
        super(id, name);
        this.varianceComponents = varianceComponents == null ? null : java.util.List.copyOf(varianceComponents);
        this.varianceInterfaces = varianceInterfaces == null ? null : java.util.List.copyOf(varianceInterfaces);
        this.varianceIssues = varianceIssues == null ? null : java.util.List.copyOf(varianceIssues);
        this.varianceResolved = varianceResolved;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceComponents() {
        return varianceComponents;
    }

    public List<StepEntity> getVarianceInterfaces() {
        return varianceInterfaces;
    }

    public List<String> getVarianceIssues() {
        return varianceIssues;
    }

    public int getVarianceResolved() {
        return varianceResolved;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceComponents", varianceComponents);
        state.put("varianceInterfaces", varianceInterfaces);
        state.put("varianceIssues", varianceIssues);
        state.put("varianceResolved", varianceResolved);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
