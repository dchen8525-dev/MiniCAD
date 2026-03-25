package com.minicad.step.model;

/**
 * Resolved ORIENTED_EDGE.
 *
 * @param id step id
 * @param name step label
 * @param edgeElement referenced base edge
 * @param orientation orientation flag
 */
public record StepOrientedEdge(int id, String name, StepEdgeCurve edgeElement, boolean orientation)
        implements StepEntity {
}
