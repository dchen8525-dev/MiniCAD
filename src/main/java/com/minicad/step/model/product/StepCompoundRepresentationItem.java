package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPOUND_REPRESENTATION_ITEM.
 */
public record StepCompoundRepresentationItem(
    int id,
    String name,
    List<StepEntity> items
) implements StepEntity {

    public StepCompoundRepresentationItem {
        items = List.copyOf(items);
    }
}
