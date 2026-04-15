package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ENVIRONMENTAL_IMPACT.
 * An environmental impact entity.
 *
 * @param id STEP instance id
 * @param name impact name
 * @param impactType impact type (energy, waste, emissions)
 * @param impactValue impact value measurement
 * @param impactUnit impact unit specification
 * @varianceTarget target variance reduction value
 * @param mitigationMeasures mitigation measures
 * @varianceStatus impact variance status
 */
public record StepEnvironmentalImpact(
    int id,
    String name,
    String impactType,
    double impactValue,
    StepEntity impactUnit,
    double varianceTarget,
    List<StepEntity> mitigationMeasures,
    String varianceStatus) implements StepEntity {
}