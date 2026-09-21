package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SUPPLY_PLAN.
 * A supply plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceItems supply variance items
 * @varianceQuantities supply variance quantities
 * @varianceSources supply variance sources
 * @varianceSchedule supply variance schedule
 * @varianceLeadTime lead variance time estimates
 * @varianceStatus plan variance status
 */
public final class StepSupplyPlan extends AbstractStepEntity {
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final List<StepEntity> varianceSources;
    private final List<StepEntity> varianceSchedule;
    private final List<Double> varianceLeadTime;
    private final String varianceStatus;

    public StepSupplyPlan(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, List<StepEntity> varianceSources, List<StepEntity> varianceSchedule, List<Double> varianceLeadTime, String varianceStatus) {
        super(id, name);
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSources = varianceSources == null ? null : java.util.List.copyOf(varianceSources);
        this.varianceSchedule = varianceSchedule == null ? null : java.util.List.copyOf(varianceSchedule);
        this.varianceLeadTime = varianceLeadTime == null ? null : java.util.List.copyOf(varianceLeadTime);
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public List<StepEntity> getVarianceSources() {
        return varianceSources;
    }

    public List<StepEntity> getVarianceSchedule() {
        return varianceSchedule;
    }

    public List<Double> getVarianceLeadTime() {
        return varianceLeadTime;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItems", varianceItems);
        state.put("varianceQuantities", varianceQuantities);
        state.put("varianceSources", varianceSources);
        state.put("varianceSchedule", varianceSchedule);
        state.put("varianceLeadTime", varianceLeadTime);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
