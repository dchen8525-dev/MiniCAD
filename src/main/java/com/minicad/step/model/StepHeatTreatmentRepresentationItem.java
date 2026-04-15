package com.minicad.step.model;

import java.util.List;

/**
 * Resolved HEAT_TREATMENT_REPRESENTATION_ITEM.
 * A heat treatment representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param treatmentType treatment variance type
 * @param treatmentTemperature treatment variance temperature
 * @param treatmentDuration treatment variance duration
 * @param treatmentStatus treatment variance status
 */
public record StepHeatTreatmentRepresentationItem(
    int id,
    String name,
    String treatmentType,
    double treatmentTemperature,
    int treatmentDuration,
    String treatmentStatus) implements StepEntity {
}