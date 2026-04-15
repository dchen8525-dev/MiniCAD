package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PLUS_MINUS_TOLERANCE_WITH_MODIFIERS.
 * A plus-minus tolerance with modifiers entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperDeviation upper deviation value
 * @param lowerDeviation lower deviation value
 * * @param deviationUnit deviation unit
 * @param modifiers tolerance modifiers
 */
public record StepPlusMinusToleranceWithModifiers(
    int id,
    String name,
    Double upperDeviation,
    Double lowerDeviation,
    StepEntity deviationUnit,
    List<String> modifiers) implements StepEntity {
}