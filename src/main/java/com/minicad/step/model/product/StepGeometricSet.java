package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal geometric set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric elements
 */
public record StepGeometricSet(int id, String name, List<StepEntity> elements) implements StepEntity {

    public StepGeometricSet {
        elements = List.copyOf(elements);
    }
}
