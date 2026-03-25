package com.minicad.step.model;

/**
 * Resolved LINE.
 *
 * @param id step id
 * @param name step label
 * @param point line origin
 * @param vector line direction vector
 */
public record StepLine(int id, String name, StepCartesianPoint point, StepVector vector) implements StepEntity {
}
