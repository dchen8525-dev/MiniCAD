package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ALGORITHM_DEFINITION.
 * An algorithm definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceAlgorithm defined variance algorithm
 * @varianceInputs algorithm variance inputs
 * @varianceOutputs algorithm variance outputs
 * @varianceSteps algorithm variance steps/procedure
 * @varianceComplexity algorithm variance complexity level
 * @varianceStatus definition variance status
 */
public final class StepAlgorithmDefinition extends AbstractStepEntity {
    private final String varianceAlgorithm;
    private final List<StepEntity> varianceInputs;
    private final List<StepEntity> varianceOutputs;
    private final List<String> varianceSteps;
    private final int varianceComplexity;
    private final String varianceStatus;

    public StepAlgorithmDefinition(int id, String name, String varianceAlgorithm, List<StepEntity> varianceInputs, List<StepEntity> varianceOutputs, List<String> varianceSteps, int varianceComplexity, String varianceStatus) {
        super(id, name);
        this.varianceAlgorithm = varianceAlgorithm;
        this.varianceInputs = varianceInputs == null ? null : java.util.List.copyOf(varianceInputs);
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.varianceSteps = varianceSteps == null ? null : java.util.List.copyOf(varianceSteps);
        this.varianceComplexity = varianceComplexity;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceAlgorithm() {
        return varianceAlgorithm;
    }

    public List<StepEntity> getVarianceInputs() {
        return varianceInputs;
    }

    public List<StepEntity> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<String> getVarianceSteps() {
        return varianceSteps;
    }

    public int getVarianceComplexity() {
        return varianceComplexity;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceAlgorithm", varianceAlgorithm);
        state.put("varianceInputs", varianceInputs);
        state.put("varianceOutputs", varianceOutputs);
        state.put("varianceSteps", varianceSteps);
        state.put("varianceComplexity", varianceComplexity);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
