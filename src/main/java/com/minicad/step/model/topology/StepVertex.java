package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal vertex marker.
 *
 * @param id STEP instance id
 * @param name inherited topological-representation-item name when available
 */
public record StepVertex(int id, String name) implements StepEntity {
}
