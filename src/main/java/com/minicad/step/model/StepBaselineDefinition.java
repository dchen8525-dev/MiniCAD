package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BASELINE_DEFINITION.
 * A baseline definition entity.
 *
 * @param id STEP instance id
 * @param name baseline name
 * @param baselineType baseline variance type
 * @param baselineValues baseline variance values
 * @param baselineUnit baseline variance unit
 * @param baselineDescription baseline variance description
 * @param baselineStatus baseline variance status
 */
public record StepBaselineDefinition(
    int id,
    String name,
    String baselineType,
    List<Double> baselineValues,
    StepEntity baselineUnit,
    String baselineDescription,
    String baselineStatus) implements StepEntity {
}