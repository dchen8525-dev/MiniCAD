package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved VALIDATION_PROPERTY_REPRESENTATION.
 * A representation used to validate geometric properties against a reference.
 */
public record StepValidationPropertyRepresentation(
    int id,
    String name,
    String representationType,
    List<StepEntity> items,
    StepEntity context
) implements StepEntity {

    public StepValidationPropertyRepresentation {
        items = List.copyOf(items);
    }
}
