package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.geometry.StepCartesianPoint;
/**
 * Resolved VERTEX_POINT.
 *
 * @param id step id
 * @param name step label
 * @param point referenced point geometry
 */
public record StepVertexPoint(int id, String name, StepCartesianPoint point) implements StepEntity {
}
