package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FILLET_DEFINITION.
 * A fillet definition entity.
 *
 * @param id STEP instance id
 * @param name fillet name
 * @param edges edges being filleted
 * @param radius fillet radius
 */
public record StepFilletDefinition(
    int id,
    String name,
    List<StepEntity> edges,
    Double radius) implements StepEntity {
}