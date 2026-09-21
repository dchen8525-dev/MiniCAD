package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CONTROL_PLAN.
 * A control plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceControlItems control variance items
 * @varianceParameters control variance parameters
 * @varianceLimits control variance limits (tolerances)
 * @varianceMethods control variance methods
 * @varianceResponse response variance actions for out-of-control
 * @varianceStatus plan variance status
 */
public final class StepControlPlan extends AbstractStepEntity {
    private final List<StepEntity> varianceControlItems;
    private final List<Double> varianceParameters;
    private final List<Double> varianceLimits;
    private final List<String> varianceMethods;
    private final List<StepEntity> varianceResponse;
    private final String varianceStatus;

    public StepControlPlan(int id, String name, List<StepEntity> varianceControlItems, List<Double> varianceParameters, List<Double> varianceLimits, List<String> varianceMethods, List<StepEntity> varianceResponse, String varianceStatus) {
        super(id, name);
        this.varianceControlItems = varianceControlItems == null ? null : java.util.List.copyOf(varianceControlItems);
        this.varianceParameters = varianceParameters == null ? null : java.util.List.copyOf(varianceParameters);
        this.varianceLimits = varianceLimits == null ? null : java.util.List.copyOf(varianceLimits);
        this.varianceMethods = varianceMethods == null ? null : java.util.List.copyOf(varianceMethods);
        this.varianceResponse = varianceResponse == null ? null : java.util.List.copyOf(varianceResponse);
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceControlItems() {
        return varianceControlItems;
    }

    public List<Double> getVarianceParameters() {
        return varianceParameters;
    }

    public List<Double> getVarianceLimits() {
        return varianceLimits;
    }

    public List<String> getVarianceMethods() {
        return varianceMethods;
    }

    public List<StepEntity> getVarianceResponse() {
        return varianceResponse;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceControlItems", varianceControlItems);
        state.put("varianceParameters", varianceParameters);
        state.put("varianceLimits", varianceLimits);
        state.put("varianceMethods", varianceMethods);
        state.put("varianceResponse", varianceResponse);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
