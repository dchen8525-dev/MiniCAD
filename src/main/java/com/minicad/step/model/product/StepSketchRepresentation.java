package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SKETCH_REPRESENTATION.
 */
public record StepSketchRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepSketchRepresentation {
        items = List.copyOf(items);
    }
}
