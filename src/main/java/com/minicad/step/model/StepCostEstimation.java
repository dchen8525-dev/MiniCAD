package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COST_ESTIMATION.
 * A cost estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (material, labor, tooling)
 * @param estimatedCost estimated cost value
 * @param costCurrency cost currency specification
 * @param costBreakdown cost breakdown items
 * @param estimationMethod estimation method used
 * @param estimationDate estimation date
 */
public final class StepCostEstimation extends AbstractStepEntity {
    private final String estimationType;
    private final double estimatedCost;
    private final String costCurrency;
    private final List<StepEntity> costBreakdown;
    private final String estimationMethod;
    private final StepEntity estimationDate;

    public StepCostEstimation(int id, String name, String estimationType, double estimatedCost, String costCurrency, List<StepEntity> costBreakdown, String estimationMethod, StepEntity estimationDate) {
        super(id, name);
        this.estimationType = estimationType;
        this.estimatedCost = estimatedCost;
        this.costCurrency = costCurrency;
        this.costBreakdown = costBreakdown == null ? null : java.util.List.copyOf(costBreakdown);
        this.estimationMethod = estimationMethod;
        this.estimationDate = estimationDate;
    }

    public String getEstimationType() {
        return estimationType;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public List<StepEntity> getCostBreakdown() {
        return costBreakdown;
    }

    public String getEstimationMethod() {
        return estimationMethod;
    }

    public StepEntity getEstimationDate() {
        return estimationDate;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("estimationType", estimationType);
        state.put("estimatedCost", estimatedCost);
        state.put("costCurrency", costCurrency);
        state.put("costBreakdown", costBreakdown);
        state.put("estimationMethod", estimationMethod);
        state.put("estimationDate", estimationDate);
        return state;
    }
}
