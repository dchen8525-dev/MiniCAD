package com.minicad.step.model;

import java.util.List;

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
public record StepCapacityPlan(
    int id,
    String name,
    List<StepEntity> varianceResources,
    List<Double> varianceCapacities,
    List<Double> varianceDemand,
    List<Double> varianceUtilization,
    String variancePeriod,
    String varianceStatus) implements StepEntity {
}