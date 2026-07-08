package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepProductionPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceProducts;
    private final List<Integer> varianceQuantities;
    private final List<StepEntity> varianceSchedule;
    private final List<StepEntity> varianceResources;
    private final String variancePeriod;
    private final String varianceStatus;

    public StepProductionPlan(int id, String name, List<StepEntity> varianceProducts, List<Integer> varianceQuantities, List<StepEntity> varianceSchedule, List<StepEntity> varianceResources, String variancePeriod, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProducts = varianceProducts == null ? null : java.util.List.copyOf(varianceProducts);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSchedule = varianceSchedule == null ? null : java.util.List.copyOf(varianceSchedule);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductionPlan that = (StepProductionPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProducts, that.varianceProducts) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceSchedule, that.varianceSchedule) && Objects.equals(varianceResources, that.varianceResources) && Objects.equals(variancePeriod, that.variancePeriod) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProducts, varianceQuantities, varianceSchedule, varianceResources, variancePeriod, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepProductionPlan{" + "id=" + id + "name=" + name + "varianceProducts=" + varianceProducts + "varianceQuantities=" + varianceQuantities + "varianceSchedule=" + varianceSchedule + "varianceResources=" + varianceResources + "variancePeriod=" + variancePeriod + "varianceStatus=" + varianceStatus + "}";
    }
}