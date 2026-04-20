package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved HYBRID_SHAPE_REPRESENTATION.
 */
public record StepHybridShapeRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepHybridShapeRepresentation {
        items = List.copyOf(items);
    }
}
