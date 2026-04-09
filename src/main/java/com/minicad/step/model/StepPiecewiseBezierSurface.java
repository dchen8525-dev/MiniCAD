package com.minicad.step.model;

/**
 * Minimal piecewise-bezier-surface marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepPiecewiseBezierSurface(int id, String name) implements StepEntity {
}
