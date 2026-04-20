package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SCHEMATIC_REPRESENTATION.
 */
public record StepSchematicRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepSchematicRepresentation {
        items = List.copyOf(items);
    }
}
