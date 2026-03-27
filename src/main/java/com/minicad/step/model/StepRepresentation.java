package com.minicad.step.model;

import java.util.List;

/**
 * Minimal representation or shape representation.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items referenced items
 * @param context representation context
 * @param shapeRepresentation whether this entity originated from SHAPE_REPRESENTATION
 */
public record StepRepresentation(
        int id,
        String name,
        List<StepEntity> items,
        StepEntity context,
        boolean shapeRepresentation
) implements StepEntity {

    public StepRepresentation {
        items = List.copyOf(items);
    }
}
