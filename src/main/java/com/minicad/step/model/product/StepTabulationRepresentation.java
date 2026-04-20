package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TABULATION_REPRESENTATION.
 */
public record StepTabulationRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepTabulationRepresentation {
        items = List.copyOf(items);
    }
}
