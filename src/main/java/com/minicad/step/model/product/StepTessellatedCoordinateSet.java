package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
/**
 * Resolved TESSELLATED_COORDINATE_SET.
 * A set of coordinates for tessellated geometry.
 */
public record StepTessellatedCoordinateSet(
    int id,
    String name,
    List<StepEntity> coordinates) implements StepEntity {
}
