package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SURFACE_TEXTURE_REPRESENTATION_ITEM.
 * A surface texture representation item entity.
 *
 * @param id STEP instance id
 * @param name item name
 * @param roughnessValue roughness value
 * @param roughnessUnit roughness unit
 * @param measurementMethod measurement method
 */
public record StepSurfaceTextureRepresentationItem(
    int id,
    String name,
    Double roughnessValue,
    StepEntity roughnessUnit,
    String measurementMethod) implements StepEntity {
}