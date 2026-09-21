package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STATE_DEFINITION.
 * A state definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceState defined variance state
 * @varianceConditions state variance conditions
 * @varianceActions state variance actions
 * @varianceTransitions state variance transitions
 * @varianceInitial initial variance state flag
 * @varianceStatus definition variance status
 */
public final class StepStateDefinition extends AbstractStepEntity {
    private final String varianceState;
    private final List<String> varianceConditions;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceTransitions;
    private final boolean varianceInitial;
    private final String varianceStatus;

    public StepStateDefinition(int id, String name, String varianceState, List<String> varianceConditions, List<StepEntity> varianceActions, List<StepEntity> varianceTransitions, boolean varianceInitial, String varianceStatus) {
        super(id, name);
        this.varianceState = varianceState;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceTransitions = varianceTransitions == null ? null : java.util.List.copyOf(varianceTransitions);
        this.varianceInitial = varianceInitial;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceState() {
        return varianceState;
    }

    public List<String> getVarianceConditions() {
        return varianceConditions;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public List<StepEntity> getVarianceTransitions() {
        return varianceTransitions;
    }

    public boolean isVarianceInitial() {
        return varianceInitial;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceState", varianceState);
        state.put("varianceConditions", varianceConditions);
        state.put("varianceActions", varianceActions);
        state.put("varianceTransitions", varianceTransitions);
        state.put("varianceInitial", varianceInitial);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
