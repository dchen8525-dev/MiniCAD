package com.minicad.step.model;

/**
 * Resolved EDGE_CURVE.
 *
 * @param id step id
 * @param name step label
 * @param start start vertex
 * @param end end vertex
 * @param edgeGeometry referenced edge geometry
 * @param sameSense orientation flag
 */
public record StepEdgeCurve(
        int id,
        String name,
        StepVertexPoint start,
        StepVertexPoint end,
        StepEntity edgeGeometry,
        boolean sameSense
) implements StepEntity {
}
