package com.minicad.step.model;

/**
 * Resolved PLANE.
 *
 * @param id step id
 * @param name step label
 * @param position plane placement
 */
public record StepPlane(int id, String name, StepAxis2Placement3D position) implements StepEntity {
}
