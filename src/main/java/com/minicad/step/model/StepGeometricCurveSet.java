package com.minicad.step.model;

import java.util.List;

/**
 * Minimal geometric curve set for PMI leaders or outlines.
 *
 * @param id STEP instance id
 * @param name set name
 * @param elements supported geometric elements
 */
public record StepGeometricCurveSet(int id, String name, List<StepEntity> elements) implements StepEntity {

    public StepGeometricCurveSet {
        elements = List.copyOf(elements);
    }
}
