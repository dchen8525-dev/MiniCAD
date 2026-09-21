package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COST_INSTANCE.
 * A cost instance entity.
 *
 * @param id STEP instance id
 * @param name cost instance name
 * @param costDefinition cost variance definition reference
 * @param costPlanned cost variance planned amount
 * @param costActual cost variance actual amount
 * @param costVariance cost variance difference
 * @param costBreakdown cost variance breakdown details
 * @param costStatus cost variance status
 */
public final class StepCostInstance extends AbstractStepEntity {
    private final StepEntity costDefinition;
    private final double costPlanned;
    private final double costActual;
    private final double costVariance;
    private final List<String> costBreakdown;
    private final String costStatus;

    public StepCostInstance(int id, String name, StepEntity costDefinition, double costPlanned, double costActual, double costVariance, List<String> costBreakdown, String costStatus) {
        super(id, name);
        this.costDefinition = costDefinition;
        this.costPlanned = costPlanned;
        this.costActual = costActual;
        this.costVariance = costVariance;
        this.costBreakdown = costBreakdown == null ? null : java.util.List.copyOf(costBreakdown);
        this.costStatus = costStatus;
    }

    public StepEntity getCostDefinition() {
        return costDefinition;
    }

    public double getCostPlanned() {
        return costPlanned;
    }

    public double getCostActual() {
        return costActual;
    }

    public double getCostVariance() {
        return costVariance;
    }

    public List<String> getCostBreakdown() {
        return costBreakdown;
    }

    public String getCostStatus() {
        return costStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("costDefinition", costDefinition);
        state.put("costPlanned", costPlanned);
        state.put("costActual", costActual);
        state.put("costVariance", costVariance);
        state.put("costBreakdown", costBreakdown);
        state.put("costStatus", costStatus);
        return state;
    }
}
