package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ZONE_REPRESENTATION.
 */
public record StepZoneRepresentation(
    int id,
    String name,
    StepEntity context,
    List<StepEntity> items
) implements StepEntity {

    public StepZoneRepresentation {
        items = List.copyOf(items);
    }
}
