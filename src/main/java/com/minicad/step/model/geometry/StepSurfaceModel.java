package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal surface-model marker.
 *
 * @param id STEP instance id
 * @param name inherited representation-item name when available
 */
public record StepSurfaceModel(int id, String name) implements StepEntity {
}
