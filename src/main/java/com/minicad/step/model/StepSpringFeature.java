package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SPRING_FEATURE.
 * A spring feature entity.
 *
 * @param id STEP instance id
 * @param name spring name
 * @param springType spring type classification (compression, extension, torsion)
 * @param wireDiameter wire diameter
 * @param coilDiameter coil (outer) diameter
 * @param numberOfCoils number of active coils
 * @param freeLength free length
 * @param springRate spring rate/constant
 * @param springMaterial spring material specification
 */
public record StepSpringFeature(
    int id,
    String name,
    String springType,
    double wireDiameter,
    double coilDiameter,
    int numberOfCoils,
    double freeLength,
    double springRate,
    StepEntity springMaterial) implements StepEntity {
}