package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CIRCLE.
 *
 * @param id step id
 * @param name step label
 * @param position circle placement
 * @param radius radius value
 */
public record StepCircle(int id, String name, StepEntity position, double radius) implements StepEntity {
}
