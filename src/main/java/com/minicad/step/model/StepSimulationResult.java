package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SIMULATION_RESULT.
 * A simulation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceModel simulation variance model reference
 * @varianceScenario simulation variance scenario
 * @varianceOutputs simulation variance output values
 * @varianceTime simulation variance time steps
 * @varianceConvergence convergence variance status
 * @varianceStatus result variance status
 */
public final class StepSimulationResult extends AbstractStepEntity {
    private final StepEntity varianceModel;
    private final StepEntity varianceScenario;
    private final List<Double> varianceOutputs;
    private final List<Double> varianceTime;
    private final boolean varianceConvergence;
    private final String varianceStatus;

    public StepSimulationResult(int id, String name, StepEntity varianceModel, StepEntity varianceScenario, List<Double> varianceOutputs, List<Double> varianceTime, boolean varianceConvergence, String varianceStatus) {
        super(id, name);
        this.varianceModel = varianceModel;
        this.varianceScenario = varianceScenario;
        this.varianceOutputs = varianceOutputs == null ? null : java.util.List.copyOf(varianceOutputs);
        this.varianceTime = varianceTime == null ? null : java.util.List.copyOf(varianceTime);
        this.varianceConvergence = varianceConvergence;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceModel() {
        return varianceModel;
    }

    public StepEntity getVarianceScenario() {
        return varianceScenario;
    }

    public List<Double> getVarianceOutputs() {
        return varianceOutputs;
    }

    public List<Double> getVarianceTime() {
        return varianceTime;
    }

    public boolean isVarianceConvergence() {
        return varianceConvergence;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceModel", varianceModel);
        state.put("varianceScenario", varianceScenario);
        state.put("varianceOutputs", varianceOutputs);
        state.put("varianceTime", varianceTime);
        state.put("varianceConvergence", varianceConvergence);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
