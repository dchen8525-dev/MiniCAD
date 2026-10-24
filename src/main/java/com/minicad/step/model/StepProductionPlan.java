package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved PRODUCTION_PLAN.
 * A production plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceProducts planned variance products
 * @varianceQuantities production variance quantities
 * @varianceSchedule production variance schedule
 * @varianceResources required variance resources
 * @variancePeriod planning variance period
 * @varianceStatus plan variance status
 */
public final class StepProductionPlan extends AbstractStepEntity {
    private final List<StepEntity> varianceProducts;
    private final List<Integer> varianceQuantities;
    private final List<StepEntity> varianceSchedule;
    private final List<StepEntity> varianceResources;
    private final String variancePeriod;
    private final String varianceStatus;

    public StepProductionPlan(int id, String name, List<StepEntity> varianceProducts, List<Integer> varianceQuantities, List<StepEntity> varianceSchedule, List<StepEntity> varianceResources, String variancePeriod, String varianceStatus) {
        super(id, name);
        this.varianceProducts = varianceProducts == null ? null : java.util.List.copyOf(varianceProducts);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSchedule = varianceSchedule == null ? null : java.util.List.copyOf(varianceSchedule);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceProducts() {
        return varianceProducts;
    }

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public List<StepEntity> getVarianceSchedule() {
        return varianceSchedule;
    }

    public List<StepEntity> getVarianceResources() {
        return varianceResources;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceProducts", varianceProducts,
                "varianceQuantities", varianceQuantities, "varianceSchedule", varianceSchedule,
                "varianceResources", varianceResources, "variancePeriod", variancePeriod,
                "varianceStatus", varianceStatus);
    }
}
