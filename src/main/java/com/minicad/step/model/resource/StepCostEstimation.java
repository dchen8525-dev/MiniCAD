package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COST_ESTIMATION.
 * A cost estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (material, labor, tooling)
 * @param estimatedCost estimated cost value
 * @param costCurrency cost currency specification
 * @param costBreakdown cost breakdown items
 * @param estimationMethod estimation method used
 * @param estimationDate estimation date
 */
public record StepCostEstimation(
    int id,
    String name,
    String estimationType,
    double estimatedCost,
    String costCurrency,
    List<StepEntity> costBreakdown,
    String estimationMethod,
    StepEntity estimationDate) implements StepEntity {
}