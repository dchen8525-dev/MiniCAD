package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved BLENDED_SURFACE.
 * A blended surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param blendingType blending type classification
 * @param primarySurface primary surface for blend
 * @param secondarySurface secondary surface for blend
 * @param blendRadius blend radius
 * @param blendCurve blend curve defining the blend path
 */
public record StepBlendedSurface(
    int id,
    String name,
    String blendingType,
    StepEntity primarySurface,
    StepEntity secondarySurface,
    double blendRadius,
    StepEntity blendCurve) implements StepEntity {
}