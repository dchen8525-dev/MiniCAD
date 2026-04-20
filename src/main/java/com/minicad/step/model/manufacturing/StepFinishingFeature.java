package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FINISHING_FEATURE.
 * A finishing feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param finishingType finishing type (polishing, grinding, honing, lapping)
 * @param surfaceGeometry surface geometry to be finished
 * @param surfaceRoughness target surface roughness (Ra)
 * @param finishingParameters finishing process parameters
 * @param finishingMaterial finishing material/tool reference
 */
public record StepFinishingFeature(
    int id,
    String name,
    String finishingType,
    StepEntity surfaceGeometry,
    double surfaceRoughness,
    List<Double> finishingParameters,
    StepEntity finishingMaterial) implements StepEntity {
}