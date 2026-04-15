package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INSPECTION_CRITERIA.
 * An inspection criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param criteriaItems list of criteria items
 * @param criteriaContext criteria context
 * @param toleranceLimits tolerance limits for each criterion
 * @param measurementMethod measurement method specifications
 */
public record StepInspectionCriteria(
    int id,
    String name,
    List<StepEntity> criteriaItems,
    StepEntity criteriaContext,
    List<Double> toleranceLimits,
    List<String> measurementMethod) implements StepEntity {
}