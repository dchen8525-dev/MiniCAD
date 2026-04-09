package com.minicad.step.model;

/**
 * Minimal piecewise-bezier-curve marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepPiecewiseBezierCurve(int id, String name) implements StepEntity {
}
