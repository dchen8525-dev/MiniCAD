package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved QUALITY_PLAN.
 * A quality plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param planId plan identifier
 * @varianceItems quality variance control items
 * @varianceMethods inspection variance methods
 * @varianceCriteria acceptance variance criteria
 * @varianceFrequency inspection variance frequency
 * @varianceStatus plan variance status
 */
public record StepQualityPlan(
    int id,
    String name,
    String planId,
    List<StepEntity> varianceItems,
    List<String> varianceMethods,
    List<StepEntity> varianceCriteria,
    String varianceFrequency,
    String varianceStatus) implements StepEntity {
}