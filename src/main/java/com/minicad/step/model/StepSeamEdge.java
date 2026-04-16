package com.minicad.step.model;

/**
 * Minimal SEAM_EDGE.
 * A seam edge where the start and end vertices are the same (closed edge on a surface seam).
 *
 * @param id STEP id
 * @param name STEP label
 * @param edgeStart start vertex
 * @param edgeEnd end vertex (same as start for seam edges)
 */
public record StepSeamEdge(
        int id,
        String name,
        StepEntity edgeStart,
        StepEntity edgeEnd
) implements StepEntity {
}
