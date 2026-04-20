package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
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
