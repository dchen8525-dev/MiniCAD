package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LUBRICATION_FEATURE.
 * A lubrication feature entity.
 *
 * @param id STEP instance id
 * @param name lubrication name
 * @param lubricationType lubrication type (oil, grease, spray)
 * @param lubricationPoints lubrication point locations
 * @param lubricationMethod lubrication method specification
 * @param lubricationInterval lubrication interval/frequency
 * @param lubricantType lubricant type specification
 */
public record StepLubricationFeature(
    int id,
    String name,
    String lubricationType,
    List<StepEntity> lubricationPoints,
    String lubricationMethod,
    String lubricationInterval,
    String lubricantType) implements StepEntity {
}