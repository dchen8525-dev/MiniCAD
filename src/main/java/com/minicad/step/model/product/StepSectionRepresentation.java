package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SECTION_REPRESENTATION.
 */
public record StepSectionRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepSectionRepresentation {
        items = List.copyOf(items);
    }
}
