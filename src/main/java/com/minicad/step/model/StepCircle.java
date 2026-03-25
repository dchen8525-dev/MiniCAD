package com.minicad.step.model;

/**
 * Resolved CIRCLE.
 *
 * @param id step id
 * @param name step label
 * @param position circle placement
 * @param radius radius value
 */
public record StepCircle(int id, String name, StepAxis2Placement3D position, double radius) implements StepEntity {
}
