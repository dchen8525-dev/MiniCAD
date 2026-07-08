package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSupplyPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final List<StepEntity> varianceSources;
    private final List<StepEntity> varianceSchedule;
    private final List<Double> varianceLeadTime;
    private final String varianceStatus;

    public StepSupplyPlan(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, List<StepEntity> varianceSources, List<StepEntity> varianceSchedule, List<Double> varianceLeadTime, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSources = varianceSources == null ? null : java.util.List.copyOf(varianceSources);
        this.varianceSchedule = varianceSchedule == null ? null : java.util.List.copyOf(varianceSchedule);
        this.varianceLeadTime = varianceLeadTime == null ? null : java.util.List.copyOf(varianceLeadTime);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSupplyPlan that = (StepSupplyPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceSources, that.varianceSources) && Objects.equals(varianceSchedule, that.varianceSchedule) && Objects.equals(varianceLeadTime, that.varianceLeadTime) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItems, varianceQuantities, varianceSources, varianceSchedule, varianceLeadTime, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepSupplyPlan{" + "id=" + id + "name=" + name + "varianceItems=" + varianceItems + "varianceQuantities=" + varianceQuantities + "varianceSources=" + varianceSources + "varianceSchedule=" + varianceSchedule + "varianceLeadTime=" + varianceLeadTime + "varianceStatus=" + varianceStatus + "}";
    }
}