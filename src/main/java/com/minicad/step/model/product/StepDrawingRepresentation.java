package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DRAWING_REPRESENTATION.
 */
public record StepDrawingRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepDrawingRepresentation {
        items = List.copyOf(items);
    }
}
