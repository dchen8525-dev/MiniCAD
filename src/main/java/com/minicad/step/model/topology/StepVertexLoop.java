package com.minicad.step.model.topology;

/**
 * Resolved VERTEX_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param loopVertex referenced single vertex
 */
public record StepVertexLoop(int id, String name, StepVertexPoint loopVertex) implements StepLoop {
}
