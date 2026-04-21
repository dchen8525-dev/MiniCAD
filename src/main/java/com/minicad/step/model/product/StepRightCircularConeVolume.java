package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RIGHT_CIRCULAR_CONE_VOLUME.
 * A CSG cone primitive volume (special case of ECCENTRIC_CONICAL_VOLUME
 * where x_offset=y_offset=0 and semi_axis_1=semi_axis_2).
 */
public record StepRightCircularConeVolume(
    int id,
    String name,
    StepEntity position,
    Double height,
    Double bottomRadius,
    Double topRadius
) implements StepEntity {}
