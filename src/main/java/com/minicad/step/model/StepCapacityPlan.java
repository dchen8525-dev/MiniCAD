package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CAPACITY_PLAN.
 * A capacity plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceResources resource variance list
 * @varianceCapacities capacity variance values
 * @varianceDemand demand variance forecast
 * @varianceUtilization utilization variance targets
 * @variancePeriod planning variance period
 * @varianceStatus plan variance status
 */
public final class StepCapacityPlan extends AbstractStepEntity {
    private final List<StepEntity> varianceResources;
    private final List<Double> varianceCapacities;
    private final List<Double> varianceDemand;
    private final List<Double> varianceUtilization;
    private final String variancePeriod;
    private final String varianceStatus;

    public StepCapacityPlan(int id, String name, List<StepEntity> varianceResources, List<Double> varianceCapacities, List<Double> varianceDemand, List<Double> varianceUtilization, String variancePeriod, String varianceStatus) {
        super(id, name);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.varianceCapacities = varianceCapacities == null ? null : java.util.List.copyOf(varianceCapacities);
        this.varianceDemand = varianceDemand == null ? null : java.util.List.copyOf(varianceDemand);
        this.varianceUtilization = varianceUtilization == null ? null : java.util.List.copyOf(varianceUtilization);
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceResources() {
        return varianceResources;
    }

    public List<Double> getVarianceCapacities() {
        return varianceCapacities;
    }

    public List<Double> getVarianceDemand() {
        return varianceDemand;
    }

    public List<Double> getVarianceUtilization() {
        return varianceUtilization;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceResources", varianceResources);
        state.put("varianceCapacities", varianceCapacities);
        state.put("varianceDemand", varianceDemand);
        state.put("varianceUtilization", varianceUtilization);
        state.put("variancePeriod", variancePeriod);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
