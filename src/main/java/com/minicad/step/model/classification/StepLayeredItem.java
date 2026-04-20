package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LAYERED_ITEM.
 * An item assigned to presentation layers.
 *
 * @param id STEP instance id
 * @param name item name
 * @param assignment layers assignment reference
 */
public record StepLayeredItem(
    int id,
    String name,
    StepEntity assignment) implements StepEntity {
}
