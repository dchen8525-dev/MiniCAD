package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HEAT_TREATMENT_SPECIFICATION.
 * A heat treatment specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationParameters specification variance parameters
 * @param specificationStatus specification variance status
 */
public record StepHeatTreatmentSpecification(
    int id,
    String name,
    String specificationType,
    String specificationDescription,
    List<String> specificationParameters,
    String specificationStatus) implements StepEntity {
}