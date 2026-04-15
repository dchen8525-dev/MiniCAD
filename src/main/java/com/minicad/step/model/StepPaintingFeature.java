package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PAINTING_FEATURE.
 * A painting feature entity.
 *
 * @param id STEP instance id
 * @param name painting name
 * @param paintType paint type classification
 * @param paintColor paint color specification
 * @param paintThickness paint thickness
 * @param appliedSurfaces surfaces to be painted
 * @param primerCoat primer coat specification
 * @varnishCoat varnish/clear coat specification
 * @param paintStandard paint standard reference
 */
public record StepPaintingFeature(
    int id,
    String name,
    String paintType,
    StepEntity paintColor,
    double paintThickness,
    List<StepEntity> appliedSurfaces,
    StepEntity primerCoat,
    StepEntity varnishCoat,
    String paintStandard) implements StepEntity {
}