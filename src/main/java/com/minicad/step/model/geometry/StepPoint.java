package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal point marker.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 */
public record StepPoint(int id, String name) implements StepEntity {
}
