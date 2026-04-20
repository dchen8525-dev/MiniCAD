package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal layer assignment.
 *
 * @param id STEP instance id
 * @param name layer name
 * @param description optional layer description
 * @param assignedItems assigned STEP items
 */
public record StepPresentationLayerAssignment(
        int id,
        String name,
        String description,
        List<StepEntity> assignedItems
) implements StepEntity {

    public StepPresentationLayerAssignment {
        assignedItems = List.copyOf(assignedItems);
    }
}
