package com.minicad.step.model;

import java.util.List;

/**
 * Minimal geometric surface set for surface collections.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric surface elements
 */
public record StepGeometricSurfaceSet(int id, String name, List<StepEntity> elements) implements StepEntity {

    public StepGeometricSurfaceSet {
        elements = List.copyOf(elements);
    }
}
