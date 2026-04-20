package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepProductionPlan(
    int id,
    String name,
    List<StepEntity> varianceProducts,
    List<Integer> varianceQuantities,
    List<StepEntity> varianceSchedule,
    List<StepEntity> varianceResources,
    String variancePeriod,
    String varianceStatus) implements StepEntity {
}