package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONTROL_PLAN.
 * A control plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceControlItems control variance items
 * @varianceParameters control variance parameters
 * @varianceLimits control variance limits (tolerances)
 * @varianceMethods control variance methods
 * @varianceResponse response variance actions for out-of-control
 * @varianceStatus plan variance status
 */
public record StepControlPlan(
    int id,
    String name,
    List<StepEntity> varianceControlItems,
    List<Double> varianceParameters,
    List<Double> varianceLimits,
    List<String> varianceMethods,
    List<StepEntity> varianceResponse,
    String varianceStatus) implements StepEntity {
}