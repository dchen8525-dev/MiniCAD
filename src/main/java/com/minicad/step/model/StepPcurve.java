package com.minicad.step.model;

/**
 * Minimal resolved PCURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param basisSurface basis surface
 * @param referenceToCurve referenced definitional representation
 */
public record StepPcurve(
        int id,
        String name,
        StepEntity basisSurface,
        StepRepresentation referenceToCurve
) implements StepEntity {
}
