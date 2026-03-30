package com.minicad.step.model;

/**
 * Resolved SURFACE_OF_REVOLUTION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve generatrix curve
 * @param axisPosition revolution axis
 */
public record StepSurfaceOfRevolution(
        int id,
        String name,
        StepEntity sweptCurve,
        StepAxis1Placement axisPosition
) implements StepEntity {
}
