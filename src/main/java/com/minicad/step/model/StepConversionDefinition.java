package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONVERSION_DEFINITION.
 * A conversion definition entity.
 *
 * @param id STEP instance id
 * @param name conversion name
 * @param conversionType conversion variance type
 * @param conversionSource conversion variance source unit/type
 * @param conversionTarget conversion variance target unit/type
 * @param conversionFactor conversion variance factor
 * @param conversionOffset conversion variance offset
 * @param conversionStatus conversion variance status
 */
public record StepConversionDefinition(
    int id,
    String name,
    String conversionType,
    StepEntity conversionSource,
    StepEntity conversionTarget,
    double conversionFactor,
    double conversionOffset,
    String conversionStatus) implements StepEntity {
}