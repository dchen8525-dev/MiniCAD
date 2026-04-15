package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MATERIAL_SPECIFICATION.
 * A material specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param materialType material type classification
 * @param materialGrade material grade specification
 * @param mechanicalProperties mechanical property values
 * @param chemicalComposition chemical composition specifications
 * @param standards applicable material standards
 */
public record StepMaterialSpecification(
    int id,
    String name,
    String materialType,
    String materialGrade,
    List<Double> mechanicalProperties,
    List<String> chemicalComposition,
    List<String> standards) implements StepEntity {
}