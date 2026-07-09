package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCapacityPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceResources;
    private final List<Double> varianceCapacities;
    private final List<Double> varianceDemand;
    private final List<Double> varianceUtilization;
    private final String variancePeriod;
    private final String varianceStatus;

    public StepCapacityPlan(int id, String name, List<StepEntity> varianceResources, List<Double> varianceCapacities, List<Double> varianceDemand, List<Double> varianceUtilization, String variancePeriod, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.varianceCapacities = varianceCapacities == null ? null : java.util.List.copyOf(varianceCapacities);
        this.varianceDemand = varianceDemand == null ? null : java.util.List.copyOf(varianceDemand);
        this.varianceUtilization = varianceUtilization == null ? null : java.util.List.copyOf(varianceUtilization);
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCapacityPlan that = (StepCapacityPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceResources, that.varianceResources) && Objects.equals(varianceCapacities, that.varianceCapacities) && Objects.equals(varianceDemand, that.varianceDemand) && Objects.equals(varianceUtilization, that.varianceUtilization) && Objects.equals(variancePeriod, that.variancePeriod) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceResources, varianceCapacities, varianceDemand, varianceUtilization, variancePeriod, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepCapacityPlan{" + "id=" + id + "name=" + name + "varianceResources=" + varianceResources + "varianceCapacities=" + varianceCapacities + "varianceDemand=" + varianceDemand + "varianceUtilization=" + varianceUtilization + "variancePeriod=" + variancePeriod + "varianceStatus=" + varianceStatus + "}";
    }
}