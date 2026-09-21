package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved QUALITY_PLAN.
 * A quality plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param planId plan identifier
 * @varianceItems quality variance control items
 * @varianceMethods inspection variance methods
 * @varianceCriteria acceptance variance criteria
 * @varianceFrequency inspection variance frequency
 * @varianceStatus plan variance status
 */
public final class StepQualityPlan extends AbstractStepEntity {
    private final String planId;
    private final List<StepEntity> varianceItems;
    private final List<String> varianceMethods;
    private final List<StepEntity> varianceCriteria;
    private final String varianceFrequency;
    private final String varianceStatus;

    public StepQualityPlan(int id, String name, String planId, List<StepEntity> varianceItems, List<String> varianceMethods, List<StepEntity> varianceCriteria, String varianceFrequency, String varianceStatus) {
        super(id, name);
        this.planId = planId;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceMethods = varianceMethods == null ? null : java.util.List.copyOf(varianceMethods);
        this.varianceCriteria = varianceCriteria == null ? null : java.util.List.copyOf(varianceCriteria);
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public String getPlanId() {
        return planId;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<String> getVarianceMethods() {
        return varianceMethods;
    }

    public List<StepEntity> getVarianceCriteria() {
        return varianceCriteria;
    }

    public String getVarianceFrequency() {
        return varianceFrequency;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("planId", planId);
        state.put("varianceItems", varianceItems);
        state.put("varianceMethods", varianceMethods);
        state.put("varianceCriteria", varianceCriteria);
        state.put("varianceFrequency", varianceFrequency);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
