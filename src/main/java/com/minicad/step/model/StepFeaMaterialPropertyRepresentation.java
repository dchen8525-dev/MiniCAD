package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FEA_MATERIAL_PROPERTY_REPRESENTATION.
 * Material properties for finite element analysis.
 */
public record StepFeaMaterialPropertyRepresentation(
    int id,
    String name,
    StepEntity material,
    List<StepEntity> properties
) implements StepEntity {

    public StepFeaMaterialPropertyRepresentation {
        properties = List.copyOf(properties);
    }
}
