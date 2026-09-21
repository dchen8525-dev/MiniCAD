package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FUNCTION_SPECIFICATION.
 * A function specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceFunction specified variance function
 * @varianceInputs function variance inputs
 * @varianceOutputs function variance outputs
 * @variancePerformance performance variance requirements
 * @varianceReliability reliability variance requirements
 * @varianceStatus specification variance status
 */
public final class StepFunctionSpecification extends AbstractStepEntity {
    private final String varianceFunction;
    private final List<StepEntity> varianceInputs;
    private final List<StepEntity> varianceOutputs;
    private final List<Double> variancePerformance;
    private final List<Double> varianceReliability;
    private final String varianceStatus;

    public StepFunctionSpecification(int id, String name, String varianceFunction, List<StepEntity> varianceInputs, List<StepEntity> varianceOutputs, List<Double> variancePerformance, List<Double> varianceReliability, String varianceStatus) {
        super(id, name);
        this.varianceFunction = varianceFunction;
        this.varianceInputs = varianceInputs == null ? null : java.util.List.copyOf(varianceInputs);
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.variancePerformance = variancePerformance == null ? null : java.util.List.copyOf(variancePerformance);
        this.varianceReliability = varianceReliability == null ? null : java.util.List.copyOf(varianceReliability);
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceFunction() {
        return varianceFunction;
    }

    public List<StepEntity> getVarianceInputs() {
        return varianceInputs;
    }

    public List<StepEntity> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<Double> getVariancePerformance() {
        return variancePerformance;
    }

    public List<Double> getVarianceReliability() {
        return varianceReliability;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceFunction", varianceFunction);
        state.put("varianceInputs", varianceInputs);
        state.put("varianceOutputs", varianceOutputs);
        state.put("variancePerformance", variancePerformance);
        state.put("varianceReliability", varianceReliability);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
