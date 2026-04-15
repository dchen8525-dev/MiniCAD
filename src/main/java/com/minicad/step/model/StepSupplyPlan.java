package com.minicad.step.model;

import java.util.List;

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
public record StepSupplyPlan(
    int id,
    String name,
    List<StepEntity> varianceItems,
    List<Integer> varianceQuantities,
    List<StepEntity> varianceSources,
    List<StepEntity> varianceSchedule,
    List<Double> varianceLeadTime,
    String varianceStatus) implements StepEntity {
}