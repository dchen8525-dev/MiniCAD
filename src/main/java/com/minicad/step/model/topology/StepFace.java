package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal face marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public record StepFace(int id, String name) implements StepEntity {
}
