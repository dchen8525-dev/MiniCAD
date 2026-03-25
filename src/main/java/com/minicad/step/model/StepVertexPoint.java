package com.minicad.step.model;

/**
 * Resolved VERTEX_POINT.
 *
 * @param id step id
 * @param name step label
 * @param point referenced point geometry
 */
public record StepVertexPoint(int id, String name, StepCartesianPoint point) implements StepEntity {
}
