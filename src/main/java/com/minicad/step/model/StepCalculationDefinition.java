package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CALCULATION_DEFINITION.
 * A calculation definition entity.
 *
 * @param id STEP instance id
 * @param name calculation name
 * @param calculationType calculation variance type
 * @param calculationMethod calculation variance method
 * @param calculationInputs calculation variance input parameters
 * @param calculationOutputs calculation variance output parameters
 * @param calculationAccuracy calculation variance accuracy
 * @param calculationStatus calculation variance status
 */
public record StepCalculationDefinition(
    int id,
    String name,
    String calculationType,
    String calculationMethod,
    List<String> calculationInputs,
    List<String> calculationOutputs,
    double calculationAccuracy,
    String calculationStatus) implements StepEntity {
}