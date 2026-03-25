package com.minicad.step.model;

/**
 * Resolved FACE_BOUND or FACE_OUTER_BOUND.
 *
 * @param id step id
 * @param name step label
 * @param loop referenced loop
 * @param orientation orientation flag
 * @param outer whether this is the outer bound
 */
public record StepFaceBound(int id, String name, StepEdgeLoop loop, boolean orientation, boolean outer)
        implements StepEntity {
}
