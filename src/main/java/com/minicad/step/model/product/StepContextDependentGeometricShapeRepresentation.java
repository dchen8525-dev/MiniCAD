package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CONTEXT_DEPENDENT_GEOMETRIC_SHAPE_REPRESENTATION.
 */
public record StepContextDependentGeometricShapeRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepContextDependentGeometricShapeRepresentation {
        items = List.copyOf(items);
    }
}
